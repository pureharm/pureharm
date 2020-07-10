package busymachines.pureharm.rest.temp

import busymachines.pureharm.anomaly._
import busymachines.pureharm.effects._
import busymachines.pureharm.rest._
import busymachines.pureharm.rest.implicits._
import org.http4s.server.blaze.BlazeServerBuilder

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jul 2020
  */
object TempRestMain extends PureharmIOApp {

  override val ioRuntime: Later[(ContextShift[IO], Timer[IO])] = IORuntime.defaultMainRuntime("test-rest")

  override def run(args: List[String]): IO[ExitCode] =
    for {
      routes <- new routes()(contextShift, timer).build
      app    = http4sApp(routes)
      server = blazeServerBuilder(app)(contextShift, timer)
      _ <- server.serve.compile.drain
    } yield ExitCode.Success

  private def http4sApp(routes: HttpRoutes[IO]): HttpApp[IO] = {

    import org.http4s.implicits._
    import org.http4s.server.Router

    Router("/" -> routes).orNotFound
  }

  private def blazeServerBuilder(
    app:         HttpApp[IO]
  )(implicit cs: ContextShift[IO], timer: Timer[IO]): BlazeServerBuilder[IO] = {
    import org.http4s.server.blaze._
    BlazeServerBuilder[IO](UnsafePools.cached("http4s"))
      .bindHttp(12345, "localhost")
      .withHttpApp(app)
  }

  private class routes(implicit contextShift: ContextShift[IO], timer: Timer[IO]) {

    def build: IO[HttpRoutes[IO]] =
      IO.raiseError(NotImplementedCatastrophe("routes"))

    object actualRoutes {

      final case class MyInputType(value: PHString)
      final case class MyOutputType(id: PHUUID, value: PHString)

      import sttp.tapir._
      import sttp.tapir.json.circe._
      import busymachines.pureharm.json.{defaultDerivationConfiguration, derive, Decoder, Encoder}
      import busymachines.pureharm.json.implicits._
      implicit val myOutputEnc: Encoder[MyOutputType] = derive.encoder[MyOutputType]
      implicit val myOutputDec: Decoder[MyOutputType] = derive.decoder[MyOutputType]
      implicit val myInputEnc:  Encoder[MyInputType]  = derive.encoder[MyInputType]
      implicit val myInputDec:  Decoder[MyInputType]  = derive.decoder[MyInputType]

      val testPath: String = "test"

      val testRouteGet: SimpleEndpoint[PHUUID, MyOutputType] =
        phEndpoint.get
          .in(testPath / path[PHUUID])
          .out(jsonBody[MyOutputType])

      val testRoutePost: SimpleEndpoint[MyInputType, MyOutputType] =
        phEndpoint.post
          .in(testPath)
          .in(jsonBody[MyInputType])
          .out(jsonBody[MyOutputType])
    }

    object http4sroutes {}
  }
}
