package busymachines.pureharm.internals.rest

import busymachines.pureharm.effects.{ContextShift, ExecutionContextCT, Sync}
import busymachines.pureharm.rest.Http4sDsl
import sttp.tapir.server.http4s.Http4sServerOptions

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
  *     import http4sRuntime._
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
abstract class Http4sRuntime[F[_], EffectType <: Sync[F]] {
  implicit def F:            EffectType
  implicit def contextShift: ContextShift[F]
  def blockingEC:            ExecutionContextCT

  import sttp.tapir.DecodeResult
  import sttp.tapir.server.{DecodeFailureContext, DecodeFailureHandler, DecodeFailureHandling, ServerDefaults}

  implicit def pureharmHTT4sServerOption: Http4sServerOptions[F] = Http4sServerOptions[F](
    createFile               = Http4sServerOptions.defaultCreateFile[F],
    blockingExecutionContext = blockingEC,
    ioChunkSize              = 8192,
    decodeFailureHandler     = ServerDefaults.decodeFailureHandler,
    logRequestHandling       = Http4sServerOptions.defaultLogRequestHandling[F],
  )

  val dsl: Http4sDsl[F] = Http4sDsl[F]
}
