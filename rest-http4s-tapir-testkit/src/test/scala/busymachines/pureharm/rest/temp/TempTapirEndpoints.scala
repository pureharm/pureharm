package busymachines.pureharm.rest.temp

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jul 2020
  */
object TempTapirEndpoints {

  import busymachines.pureharm.rest._

  val testPath: String = "test"

  //TEMP IMPLEMENTATION
  //-------------------------------------------------
  import busymachines.pureharm.anomaly._
  import busymachines.pureharm.effects._
  import busymachines.pureharm.effects.implicits._

  final class TestHttp4sRuntime[F[_]](
    override val blockingEC:   ExecutionContextCT
  )(implicit
    override val F:            Sync[F],
    override val contextShift: ContextShift[F],
  ) extends Http4sRuntime[F, Sync[F]] {

    override val http4sServerOptions: Http4sServerOptions[F] =
      super.http4sServerOptions.withCustomHeaderAuthValidation(AuthTokenHeaderName)
  }

  object TestHttp4sRuntime {

    def apply[F[_]](ec: ExecutionContextCT)(implicit f: Sync[F], cs: ContextShift[F]): TestHttp4sRuntime[F] =
      new TestHttp4sRuntime(ec)
  }

  //using this over Authentication to double as CSRF token as well
  private lazy val AuthTokenHeaderName = "X-AUTH-TOKEN"

  val authedEndpoint: SimpleEndpoint[MyAuthToken, Unit] =
    phEndpoint.in(auth.xCustomAuthHeader[MyAuthToken](AuthTokenHeaderName))

  val bearerAuthedEndpoint: SimpleEndpoint[MyAuthToken, Unit] =
    phEndpoint.in(auth.bearer[MyAuthToken])

  val apiKeyAuthedEndpoint: SimpleEndpoint[MyAuthToken, Unit] =
    phEndpoint.in(auth.apiKey[MyAuthToken](query[MyAuthToken]("apiKey")))

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
        id = id,
        f1 = PHString.unsafeGenerate,
        f2 = PHInt.unsafeGenerate,
        f3 = PHLong.unsafeGenerate,
        fl = List(PHLong.unsafeGenerate),
        f4 = List(PHUUID.unsafeGenerate, PHUUID.unsafeGenerate, PHUUID.unsafeGenerate),
        f5 = Option(PHString.unsafeGenerate),
      )
    }

    def postLogic(input: MyInputType)(implicit auth: MyAuthToken): F[MyOutputType] =
      for {
        ctx <- authStack.authenticate(auth)
        _   <- F.delay(println(s"I AM $ctx"))
        _   <- Sync[F].delay(println(s"POST LOGIC HERE — $input"))
        id  <- PHUUID.generate[F]
      } yield MyOutputType(
        id = id,
        f1 = input.f1,
        f2 = input.f2,
        f3 = input.f3,
        fl = input.fl,
        f4 = input.f4,
        f5 = input.f5,
      )
  }

  ////------------------------------------------------

  trait MyAppRest[F[_]] extends RestDefs[F, Sync[F], TestHttp4sRuntime[F]]

  final class SomeAPI[F[_]](
    domain:                              SomeOrganizer[F]
  )(implicit override val http4sRuntime: TestHttp4sRuntime[F])
    extends MyAppRest[F] {

    val nonAuthedGetEndpoint: SimpleEndpoint[Unit, MyOutputType] = phEndpoint.get
      .in("no_auth")
      .out(jsonBody[MyOutputType])
      .out(statusCode(StatusCode.Ok))

    val nonAuthedPostEndpoint: SimpleEndpoint[MyInputType, MyOutputType] = phEndpoint.post
      .in("no_auth")
      .in(jsonBody[MyInputType])
      .out(jsonBody[MyOutputType])
      .out(statusCode(StatusCode.Created))

    val testGetEndpoint: SimpleEndpoint[(MyAuthToken, PHUUID), MyOutputType] = authedEndpoint.get
      .in("test" / path[PHUUID])
      .out(jsonBody[MyOutputType])
      .out(statusCode(StatusCode.Ok))

    val testPostEndpoint: SimpleEndpoint[(MyAuthToken, MyInputType), MyOutputType] = authedEndpoint.post
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

    val testGetWithHeaderEndpoint: SimpleEndpoint[(MyAuthToken, PHUUID, PHHeader), MyOutputType] = authedEndpoint.get
      .in("test_h" / path[PHUUID])
      .in(header[PHHeader]("X-TEST-HEADER"))
      .out(jsonBody[MyOutputType])
      .out(statusCode(StatusCode.Ok))

    val testNotFound: SimpleEndpoint[MyAuthToken, MyOutputType] = authedEndpoint.get
      .in("test_logic" / "not_found")
      .out(jsonBody[MyOutputType])
      .out(statusCode(StatusCode.Ok))

    val testUnauthorized: SimpleEndpoint[MyAuthToken, MyOutputType] = authedEndpoint.get
      .in("test_logic" / "unauthorized")
      .out(jsonBody[MyOutputType])
      .out(statusCode(StatusCode.Ok))

    val testForbidden: SimpleEndpoint[MyAuthToken, MyOutputType] = authedEndpoint.get
      .in("test_logic" / "forbidden")
      .out(jsonBody[MyOutputType])
      .out(statusCode(StatusCode.Ok))

    val testDenied: SimpleEndpoint[MyAuthToken, MyOutputType] = authedEndpoint.get
      .in("test_logic" / "denied")
      .out(jsonBody[MyOutputType])
      .out(statusCode(StatusCode.Ok))

    val testInvalidInput: SimpleEndpoint[MyAuthToken, MyOutputType] = authedEndpoint.get
      .in("test_logic" / "invalid_input")
      .out(jsonBody[MyOutputType])
      .out(statusCode(StatusCode.Ok))

    val testConflict: SimpleEndpoint[MyAuthToken, MyOutputType] = authedEndpoint.get
      .in("test_logic" / "conflict")
      .out(jsonBody[MyOutputType])
      .out(statusCode(StatusCode.Ok))

    val testAnomalies: SimpleEndpoint[MyAuthToken, MyOutputType] = authedEndpoint.get
      .in("test_logic" / "anomalies")
      .out(jsonBody[MyOutputType])
      .out(statusCode(StatusCode.Ok))

    val testNotImplemented: SimpleEndpoint[MyAuthToken, MyOutputType] = authedEndpoint.get
      .in("test_logic" / "not_implemented")
      .out(jsonBody[MyOutputType])
      .out(statusCode(StatusCode.Ok))

    val testCatastrophe: SimpleEndpoint[MyAuthToken, MyOutputType] = authedEndpoint.get
      .in("test_logic" / "catastrophe")
      .out(jsonBody[MyOutputType])
      .out(statusCode(StatusCode.Ok))

    val testNormalThrowable: SimpleEndpoint[MyAuthToken, MyOutputType] = authedEndpoint.get
      .in("test_logic" / "throwable")
      .out(jsonBody[MyOutputType])
      .out(statusCode(StatusCode.Ok))

    import sttp.tapir.server.http4s._

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

    val testNotFoundRoute: HttpRoutes[F] = testNotFound.toRouteRecoverErrors { _ =>
      F.raiseError[MyOutputType](NotFoundAnomaly("test_not_found"))
    }

    val testUnauthorizedRoute: HttpRoutes[F] = testUnauthorized.toRouteRecoverErrors { _ =>
      F.raiseError[MyOutputType](UnauthorizedAnomaly("test_unauthorized"))
    }

    val testForbiddenRoute: HttpRoutes[F] = testForbidden.toRouteRecoverErrors { _ =>
      F.raiseError[MyOutputType](ForbiddenAnomaly("test_forbidden"))
    }

    val testDeniedRoute: HttpRoutes[F] = testDenied.toRouteRecoverErrors { _ =>
      F.raiseError[MyOutputType](DeniedAnomaly("test_denied"))
    }

    val testInvalidInputRoute: HttpRoutes[F] = testInvalidInput.toRouteRecoverErrors { _ =>
      F.raiseError[MyOutputType](InvalidInputAnomaly("test_invalid_input"))
    }

    val testConflictRoute: HttpRoutes[F] = testConflict.toRouteRecoverErrors { _ =>
      F.raiseError[MyOutputType](ConflictAnomaly("test_conflict"))
    }

    private case object TestAnomaliesID extends AnomalyID { override val name: String = "test_anomalies" }

    val testAnomaliesRoute: HttpRoutes[F] = testAnomalies.toRouteRecoverErrors { _ =>
      F.raiseError[MyOutputType](
        Anomalies(
          TestAnomaliesID,
          "test_anomalies",
          ConflictAnomaly("test_conflict"),
          InvalidInputAnomaly("test_invalid_input"),
        )
      )
    }

    val testNotImplementedRoute: HttpRoutes[F] = testNotImplemented.toRouteRecoverErrors { _ =>
      F.raiseError[MyOutputType](NotImplementedCatastrophe("not_implemented"))
    }

    val testCatastropheRoute: HttpRoutes[F] = testCatastrophe.toRouteRecoverErrors { _ =>
      F.raiseError[MyOutputType](InconsistentStateCatastrophe("catastrophe"))
    }

    val testNormalThrowableRoute: HttpRoutes[F] = testNormalThrowable.toRouteRecoverErrors { _ =>
      F.raiseError[MyOutputType](new RuntimeException("throwable"))
    }

    val routes: HttpRoutes[F] = NEList
      .of[HttpRoutes[F]](
        nonAuthedGetRoute,
        nonAuthedPostRoute,
        testGetRoute,
        testGetEndpointQueryParamsRoute,
        testGetWithHeaderRoute,
        testPostRoute,
        testNotFoundRoute,
        testUnauthorizedRoute,
        testForbiddenRoute,
        testDeniedRoute,
        testInvalidInputRoute,
        testConflictRoute,
        testAnomaliesRoute,
        testNotImplementedRoute,
        testCatastropheRoute,
        testNormalThrowableRoute,
      )
      .reduceK

  }

}
