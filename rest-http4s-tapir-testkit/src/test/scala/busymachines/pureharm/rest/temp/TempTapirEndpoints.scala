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
package busymachines.pureharm.rest.temp

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jul 2020
  */
object TempTapirEndpoints {

  import busymachines.pureharm.effects._
  import busymachines.pureharm.effects.implicits._

  case class MyAuthContext(token: MyAuthToken)

  final class MyAuthStack[F[_]](implicit F: Applicative[F]) {
    def authenticate(token: MyAuthToken): F[MyAuthContext] = F.pure(MyAuthContext(token))
  }

  final class SomeOrganizer[F[_]]()(implicit val F: Sync[F], authStack: MyAuthStack[F]) {

    def withAuth[T](f: MyAuthContext => F[T])(implicit token: MyAuthToken, authStack: MyAuthStack[F]): F[T] =
      for {
        ctx    <- authStack.authenticate(token)
        result <- f(ctx)
      } yield result

    def getLogic(id: PHUUID)(implicit auth: MyAuthToken): F[MyOutputType] = withAuth { ctx: MyAuthContext =>
      for {
        _ <- F.delay(println(s"I AM $ctx"))
        _ <- F.delay(println(s"GET LOGIC HERE — $id"))
      } yield MyOutputType(
        id  = id,
        f1  = PHString.unsafeGenerate,
        f2  = PHInt.unsafeGenerate,
        f3  = PHLong.unsafeGenerate,
        fl  = List(PHLong.unsafeGenerate),
        f4  = List(PHUUID.unsafeGenerate, PHUUID.unsafeGenerate, PHUUID.unsafeGenerate),
        f5  = Option(PHString.unsafeGenerate),
        sf6 = SafePHUUIDStr.unsafeGenerate,
        sf7 = SafePHUUIDThr.unsafeGenerate,
      )
    }

    def postLogic(input: MyInputType)(implicit auth: MyAuthToken): F[MyOutputType] =
      for {
        ctx <- authStack.authenticate(auth)
        _   <- F.delay(println(s"I AM $ctx"))
        _   <- Sync[F].delay(println(s"POST LOGIC HERE — $input"))
        id  <- PHUUID.generate[F]
      } yield MyOutputType(
        id  = id,
        f1  = input.f1,
        f2  = input.f2,
        f3  = input.f3,
        fl  = input.fl,
        f4  = input.f4,
        f5  = input.f5,
        sf6 = input.sf6,
        sf7 = input.sf7,
      )
  }

  import busymachines.pureharm.rest._

  //using this over Authentication to double as CSRF token as well
  private lazy val AuthTokenHeaderName = "X-AUTH-TOKEN"

  //----- Create a unified way of doing auth, you can also add in common logic. See tapir docs for that
  val authedEndpoint: SimpleEndpoint[MyAuthToken, Unit] =
    phEndpoint.in(auth.xCustomAuthHeader[MyAuthToken](AuthTokenHeaderName))

  //----- Create one of these for your app
  final class TestHttp4sRuntime[F[_]](
    override val blockingShifter: BlockingShifter[F]
  )(implicit
    override val F:               Sync[F]
  ) extends Http4sRuntime[F, Sync[F]] {

    override val http4sServerOptions: Http4sServerOptions[F] =
      super.http4sServerOptions.withCustomHeaderAuthValidation(AuthTokenHeaderName)
  }

  object TestHttp4sRuntime {

    def apply[F[_]](
      blockingShifter: BlockingShifter[F]
    )(implicit f:      Sync[F]): TestHttp4sRuntime[F] =
      new TestHttp4sRuntime(blockingShifter)
  }

  ////------------------------------------------------

  // ----- Create one of these for your app
  trait MyAppRest[F[_]] extends RestDefs[F, Sync[F], TestHttp4sRuntime[F]]

  // ----- And create as many of these as you need
  final class SomeAPI[F[_]](
    domain:                              SomeOrganizer[F]
  )(implicit override val http4sRuntime: TestHttp4sRuntime[F])
    extends MyAppRest[F] {

    val nonAuthedGetEndpoint: SimpleEndpoint[Unit, MyOutputType] =
      phEndpoint.get
        .in("no_auth")
        .out(jsonBody[MyOutputType])
        .out(statusCode(StatusCode.Ok))

    val nonAuthedPostEndpoint: SimpleEndpoint[MyInputType, MyOutputType] =
      phEndpoint.post
        .in("no_auth")
        .in(jsonBody[MyInputType])
        .out(jsonBody[MyOutputType])
        .out(statusCode(StatusCode.Created))

    val testGetEndpoint: SimpleEndpoint[(MyAuthToken, PHUUID), MyOutputType] =
      authedEndpoint.get
        .in("test" / path[PHUUID])
        .out(jsonBody[MyOutputType])
        .out(statusCode(StatusCode.Ok))

    //Unfortunately, providing implicitly such codecs degrades compilation by one order of magnitude.
    implicit private val c: TapirPlainCodec[SafePHUUIDThr] =
      TapirCodecs.safePhantomTypePlainCodec[java.util.UUID, SafePHUUIDThr]

    val testGetEndpointSafePHUUIDThr: SimpleEndpoint[(MyAuthToken, SafePHUUIDThr), MyOutputType] =
      authedEndpoint.get
        .in("test_safe_thr" / path[SafePHUUIDThr])
        .out(jsonBody[MyOutputType])
        .out(statusCode(StatusCode.Ok))

    val testPostEndpoint: SimpleEndpoint[(MyAuthToken, MyInputType), MyOutputType] =
      authedEndpoint.post
        .in("test")
        .in(jsonBody[MyInputType])
        .out(jsonBody[MyOutputType])
        .out(statusCode(StatusCode.Created))

    val testGetEndpointQueryParams: SimpleEndpoint[(MyAuthToken, PHUUID, PHLong, Option[PHInt]), MyOutputType] =
      authedEndpoint.get
        .in("test_q" / path[PHUUID])
        .in(query[PHLong]("long"))
        .in(query[Option[PHInt]]("opt_int"))
        .out(jsonBody[MyOutputType])
        .out(statusCode(StatusCode.Ok))

    val testGetWithHeaderEndpoint: SimpleEndpoint[(MyAuthToken, PHUUID, PHHeader), MyOutputType] =
      authedEndpoint.get
        .in("test_h" / path[PHUUID])
        .in(header[PHHeader]("X-TEST-HEADER"))
        .out(jsonBody[MyOutputType])
        .out(statusCode(StatusCode.Ok))

    val nonAuthedGetRoute: HttpRoutes[F] = nonAuthedGetEndpoint.toRouteRecoverErrors { _ =>
      domain.getLogic(PHUUID.unsafeGenerate)(MyAuthToken.unsafeGenerate)
    }

    val nonAuthedPostRoute: HttpRoutes[F] = nonAuthedPostEndpoint.toRouteRecoverErrors {
      case (myInputType: MyInputType) => domain.postLogic(myInputType)(MyAuthToken.unsafeGenerate)
    }

    val testGetRoute: HttpRoutes[F] = testGetEndpoint.toRouteRecoverErrors {
      case (auth: MyAuthToken, ph: PHUUID) => domain.getLogic(ph)(auth)
    }

    val testGetEndpointQueryParamsRoute: HttpRoutes[F] = testGetEndpointQueryParams.toRouteRecoverErrors {
      case (auth: MyAuthToken, ph: PHUUID, longParam: PHLong, intOpt: Option[PHInt]) =>
        F.delay[Unit](println(s"params: $longParam --- $intOpt")) >>
          domain.getLogic(ph)(auth)
    }

    val testGetWithHeaderRoute: HttpRoutes[F] = testGetWithHeaderEndpoint.toRouteRecoverErrors {
      case (auth: MyAuthToken, ph: PHUUID, header: PHHeader) =>
        F.delay[Unit](println(s"header: $header")) >> domain.getLogic(ph)(auth)
    }

    val testPostRoute: HttpRoutes[F] = testPostEndpoint.toRouteRecoverErrors {
      case (auth: MyAuthToken, myInputType: MyInputType) => domain.postLogic(myInputType)(auth)
    }

    val routes: HttpRoutes[F] = NEList
      .of[HttpRoutes[F]](
        nonAuthedGetRoute,
        nonAuthedPostRoute,
        testGetRoute,
        testGetEndpointQueryParamsRoute,
        testGetWithHeaderRoute,
        testPostRoute,
      )
      .reduceK
  }

}
