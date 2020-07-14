package busymachines.pureharm.rest.temp

import sttp.model.StatusCode
import sttp.tapir.server.PartialServerEndpoint

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jul 2020
  */
object TempTapirEndpoints {

  import busymachines.pureharm.rest._
  import busymachines.pureharm.rest.implicits._
  import sttp.tapir._
  import sttp.tapir.json.circe._

  val testPath: String = "test"

  //TEMP IMPLEMENTATION
  //-------------------------------------------------
  import busymachines.pureharm.anomaly._
  import busymachines.pureharm.phantom._
  import busymachines.pureharm.effects._
  import busymachines.pureharm.effects.implicits._

  final class TestHttp4sRuntime[F[_]](
    override val blockingEC:   ExecutionContextCT
  )(implicit
    override val F:            Sync[F],
    override val contextShift: ContextShift[F],
  ) extends Http4sRuntime[F, Sync[F]] {}

  object TestHttp4sRuntime {

    def apply[F[_]](ec: ExecutionContextCT)(implicit f: Sync[F], cs: ContextShift[F]): TestHttp4sRuntime[F] =
      new TestHttp4sRuntime(ec)
  }

  implicit class TempOps(o: TapirAuth.type) {

    private val Bearer  = "Bearer"
    private val Bearer_ = s"$Bearer "

    def xBearer[T: Codec[List[String], *, CodecFormat.TextPlain]](
      headerName: String
    ): EndpointInput.Auth.Http[T] = {
      val codec     = implicitly[Codec[List[String], T, CodecFormat.TextPlain]]
      val authCodec = Codec.list(Codec.string.map(Mapping.stringPrefix(Bearer_))).map(codec).schema(codec.schema)
      EndpointInput.Auth.Http(Bearer, header[T](headerName)(authCodec))
    }

  }

  object MyAuthToken extends PhantomType[String]
  type MyAuthToken = MyAuthToken.Type

  private val AuthTokenHeaderName = "X-AUTH-TOKEN" //using this over Authentication to double as CSRF token as well

  type AuthedEndpoint[F[_], In, Out] = PartialServerEndpoint[MyAuthContext, In, AnomalyBase, Out, Nothing, F]

  val authedEndpoint: SimpleEndpoint[MyAuthToken, Unit] =
    phEndpoint.in(auth.xBearer[MyAuthToken](AuthTokenHeaderName))

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

    val testGetEndpoint: SimpleEndpoint[(MyAuthToken, PHUUID), MyOutputType] = authedEndpoint.get
      .in("test" / path[PHUUID])
      .out(jsonBody[MyOutputType])
      .out(statusCode(StatusCode.Ok))

    val testGetEndpointQueryParams: SimpleEndpoint[(MyAuthToken, PHUUID, PHLong, Option[PHInt]), MyOutputType] =
      authedEndpoint.get
        .in("test_q" / path[PHUUID])
        .in(query[PHLong]("long"))
        .in(query[Option[PHInt]]("opt_int"))
        .out(jsonBody[MyOutputType])
        .out(statusCode(StatusCode.Ok))

    val testPostEndpoint: SimpleEndpoint[(MyAuthToken, MyInputType), MyOutputType] = authedEndpoint.post
      .in("test")
      .in(jsonBody[MyInputType])
      .out(jsonBody[MyOutputType])
      .out(statusCode(StatusCode.Created))

    val testNotFound: SimpleEndpoint[MyAuthToken, MyOutputType] = authedEndpoint.get
      .in("test" / "not_found")
      .out(jsonBody[MyOutputType])
      .out(statusCode(StatusCode.Ok))

    val testUnauthorized: SimpleEndpoint[MyAuthToken, MyOutputType] = authedEndpoint.get
      .in("test" / "unauthorized")
      .out(jsonBody[MyOutputType])
      .out(statusCode(StatusCode.Ok))

    val testForbidden: SimpleEndpoint[MyAuthToken, MyOutputType] = authedEndpoint.get
      .in("test" / "forbidden")
      .out(jsonBody[MyOutputType])
      .out(statusCode(StatusCode.Ok))

    val testDenied: SimpleEndpoint[MyAuthToken, MyOutputType] = authedEndpoint.get
      .in("test" / "denied")
      .out(jsonBody[MyOutputType])
      .out(statusCode(StatusCode.Ok))

    val testInvalidInput: SimpleEndpoint[MyAuthToken, MyOutputType] = authedEndpoint.get
      .in("test" / "invalid_input")
      .out(jsonBody[MyOutputType])
      .out(statusCode(StatusCode.Ok))

    val testConflict: SimpleEndpoint[MyAuthToken, MyOutputType] = authedEndpoint.get
      .in("test" / "conflict")
      .out(jsonBody[MyOutputType])
      .out(statusCode(StatusCode.Ok))

    val testAnomalies: SimpleEndpoint[MyAuthToken, MyOutputType] = authedEndpoint.get
      .in("test" / "anomalies")
      .out(jsonBody[MyOutputType])
      .out(statusCode(StatusCode.Ok))

    val testNotImplemented: SimpleEndpoint[MyAuthToken, MyOutputType] = authedEndpoint.get
      .in("test" / "not_implemented")
      .out(jsonBody[MyOutputType])
      .out(statusCode(StatusCode.Ok))

    val testCatastrophe: SimpleEndpoint[MyAuthToken, MyOutputType] = authedEndpoint.get
      .in("test" / "catastrophe")
      .out(jsonBody[MyOutputType])
      .out(statusCode(StatusCode.Ok))

    val testNormalThrowable: SimpleEndpoint[MyAuthToken, MyOutputType] = authedEndpoint.get
      .in("test" / "throwable")
      .out(jsonBody[MyOutputType])
      .out(statusCode(StatusCode.Ok))

    import sttp.tapir.server.http4s._

    val testGetRoute: HttpRoutes[F] = testGetEndpoint.toRouteRecoverErrors {
      case (auth: MyAuthToken, ph: PHUUID) => domain.getLogic(ph)(auth)
    }

    val testGetEndpointQueryParamsRoute: HttpRoutes[F] = testGetEndpointQueryParams.toRouteRecoverErrors {
      case (auth: MyAuthToken, ph: PHUUID, longParam: PHLong, intOpt: Option[PHInt]) =>
        F.delay[Unit](println(s"params: $longParam --- $intOpt")) >>
          domain.getLogic(ph)(auth)
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
        testGetRoute,
        testGetEndpointQueryParamsRoute,
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
