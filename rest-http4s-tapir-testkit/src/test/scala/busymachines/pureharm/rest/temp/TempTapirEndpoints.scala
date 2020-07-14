package busymachines.pureharm.rest.temp

import sttp.tapir.server.{PartialServerEndpoint, ServerDefaults}
import sttp.tapir.server.http4s.Http4sServerOptions

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

  abstract class Http4sRuntime[F[_], EffectType <: Sync[F]] {
    implicit def F:            EffectType
    implicit def contextShift: ContextShift[F]
    def blockingEC:            ExecutionContextCT

    implicit def pureharmHTT4sServerOption: Http4sServerOptions[F] = Http4sServerOptions[F](
      createFile               = Http4sServerOptions.defaultCreateFile[F],
      blockingExecutionContext = blockingEC,
      ioChunkSize              = 8192,
      decodeFailureHandler     = ServerDefaults.decodeFailureHandler,
      logRequestHandling       = Http4sServerOptions.defaultLogRequestHandling[F],
    )

    val dsl: Http4sDsl[F] = Http4sDsl[F]
  }

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
        id = PHUUID.unsafeGenerate,
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

  final class SomeAPI[F[_]](
    domain:                     SomeOrganizer[F]
  )(implicit val http4sRuntime: TestHttp4sRuntime[F]) {

    import http4sRuntime._

    val testGetEndpoint: SimpleEndpoint[(MyAuthToken, PHUUID), MyOutputType] = authedEndpoint.get
      .in("test" / path[PHUUID])
      .out(jsonBody[MyOutputType])

    val testPostEndpoint: SimpleEndpoint[(MyAuthToken, MyInputType), MyOutputType] = authedEndpoint.post
      .in("test")
      .in(jsonBody[MyInputType])
      .out(jsonBody[MyOutputType])

    import sttp.tapir.server.http4s._

    val testGetRoute: HttpRoutes[F] = testGetEndpoint.toRoutes {
      case (auth: MyAuthToken, ph: PHUUID) => domain.getLogic(ph)(auth).attemptAnomaly
    }

    val testPostRoute: HttpRoutes[F] = testPostEndpoint.toRoutes {
      case (auth: MyAuthToken, myInputType: MyInputType) => domain.postLogic(myInputType)(auth).attemptAnomaly
    }

    val routes: HttpRoutes[F] = testGetRoute <+> testPostRoute

  }

}
