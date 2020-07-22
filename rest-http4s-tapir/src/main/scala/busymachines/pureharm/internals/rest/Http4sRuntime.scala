/**
  * Copyright (c) 2017-2019 BusyMachines
  *
  * See company homepage at: https://www.busymachines.com/
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package busymachines.pureharm.internals.rest

import scala.annotation.implicitNotFound

import busymachines.pureharm.effects.{ContextShift, ExecutionContextCT, Sync}
import sttp.tapir.server.http4s.Http4sServerOptions
import sttp.tapir.server.{DecodeFailureContext, DecodeFailureHandling}

/**
  * Encapsulates all things needed to translate
  * tapir Endpoints to http4s Route.
  *
  * This abstract class should be extended and the EffectType
  * fixed. http4s needs at least [[Sync]], but sometimes in your
  * specific app you might need a stronger constraint, and
  * you ought to define subclasses of this with stronger
  * constraints, and propagate those.
  *
  * E.g.
  * {{{
  *     final class TestHttp4sRuntime[F[_]](
  *     override val blockingEC:   ExecutionContextCT
  *   )(implicit
  *     override val F:            Sync[F],
  *     override val contextShift: ContextShift[F],
  *   ) extends Http4sRuntime[F, Sync[F]] {}
  *
  *   object TestHttp4sRuntime {
  *     def apply[F[_]](ec: ExecutionContextCT)(implicit f: Sync[F], cs: ContextShift[F]): TestHttp4sRuntime[F] =
  *       new TestHttp4sRuntime(ec)
  *   }
  * }}}
  *
  * And in http4s route definitions, use this:
  *
  * {{{
  *    final class SomeAPI[F[_]](
  *     domain:                     SomeOrganizer[F]
  *   )(implicit val http4sRuntime: TestHttp4sRuntime[F]) {
  *
  *     import http4sRuntime._ //or extend RestDef which provides implicit forwarders to the runtime
  *
  *     val testGetEndpoint: SimpleEndpoint[(MyAuthToken, PHUUID), MyOutputType] = authedEndpoint.get
  *       .in("test" / path[PHUUID])
  *       .out(jsonBody[MyOutputType])
  *
  *     val testPostEndpoint: SimpleEndpoint[(MyAuthToken, MyInputType), MyOutputType] = authedEndpoint.post
  *       .in("test")
  *       .in(jsonBody[MyInputType])
  *       .out(jsonBody[MyOutputType])
  *
  *     import sttp.tapir.server.http4s._
  *
  *     val testGetRoute: HttpRoutes[F] = testGetEndpoint.toRoutes {
  *       case (auth: MyAuthToken, ph: PHUUID) => domain.getLogic(ph)(auth).attemptAnomaly
  *     }
  *
  *     val testPostRoute: HttpRoutes[F] = testPostEndpoint.toRoutes {
  *       case (auth: MyAuthToken, myInputType: MyInputType) => domain.postLogic(myInputType)(auth).attemptAnomaly
  *     }
  *
  *     val routes: HttpRoutes[F] = testGetRoute <+> testPostRoute
  *
  *   }
  * }}}
  */
@implicitNotFound(
  // format: off
"""
The purpose of an Http4sRuntime is to bundle in everything you need to be able to define http4s routes,
both on their own and/or using tapir.

Possible reasons why you can't find it:
  - usually, you subclass the abstract Http4sRuntime in your app and you instantiate it only once.
  - so search where it is instantiate it, and make sure you propagate it to where it's needed

See scaladoc for more information
"""
  // format: on
)
abstract class Http4sRuntime[F[_], EffectType <: Sync[F]] {
  implicit def F:            EffectType
  implicit def contextShift: ContextShift[F]
  def blockingEC:            ExecutionContextCT

  implicit def http4sServerOptions: Http4sServerOptions[F] = _defaultOps

  private[this] lazy val _defaultOps = Http4sServerOptions[F](
    createFile               = Http4sServerOptions.defaultCreateFile[F],
    blockingExecutionContext = blockingEC,
    ioChunkSize              = 8192,
    decodeFailureHandler     = PureharmTapirDecodeFailureHandler.handler(), //ServerDefaults.decodeFailureHandler,
    logRequestHandling       = Http4sServerOptions
      .defaultLogRequestHandling[F]
      .copy(
        logLogicExceptions = false
      ),
  )

  val dsl: org.http4s.dsl.Http4sDsl[F] = org.http4s.dsl.Http4sDsl[F]

  implicit protected class OptionsOps(ops: Http4sServerOptions[F]) {

    def withCustomHeaderAuthValidation(headerName: String): Http4sServerOptions[F] =
      this.withAuthValidation(PureharmTapirDecodeFailureHandler.missingCustomHeaderAuth(headerName))

    def withBearerAuthValidation: Http4sServerOptions[F] =
      this.withAuthValidation(PureharmTapirDecodeFailureHandler.missingBearerAuth)

    def withApiKeyAuthValidation: Http4sServerOptions[F] =
      this.withAuthValidation(PureharmTapirDecodeFailureHandler.missingApiKeyAuth)

    def withAuthValidation(f: DecodeFailureContext => Option[DecodeFailureHandling]): Http4sServerOptions[F] =
      ops.copy(
        decodeFailureHandler = PureharmTapirDecodeFailureHandler.handler(
          missingOrInvalidAuth = f
        )
      )
  }

}
