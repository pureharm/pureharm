package busymachines.pureharm.rest.temp

import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._
import busymachines.pureharm.rest._

import org.http4s.server.blaze.BlazeServerBuilder

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jul 2020
  */
object TempRestMain extends PureharmIOApp {

  override val ioRuntime: Later[(ContextShift[IO], Timer[IO])] = IORuntime.defaultMainRuntime("test-rest")
  import busymachines.pureharm.rest.temp.TempTapirEndpoints._

  override def run(args: List[String]): IO[ExitCode] = {
    implicit val CE: ConcurrentEffect[IO] = IO.ioConcurrentEffect(contextShift)
    for {
      http4sRuntime <- TestHttp4sRuntime[IO](UnsafePools.cached("htt4s"))(Sync[IO], contextShift).pure[IO]
      app           <- MyAppEcology.everything[IO](Sync[IO], http4sRuntime)
      _             <- MyAppDocs.printYAML[IO](app.restAPIs)
      blazeServer = blazeServerBuilder[IO](app.http4sApp)(CE, timer)
      _ <- blazeServer.serve.compile.drain
    } yield ExitCode.Success
  }

  private def blazeServerBuilder[F[_]](
    app:        HttpApp[F]
  )(implicit F: ConcurrentEffect[F], timer: Timer[F]): BlazeServerBuilder[F] = {
    import org.http4s.server.blaze._
    BlazeServerBuilder[F](UnsafePools.cached("http4s"))
      .bindHttp(12345, "localhost")
      .withHttpApp(app)
  }

}
