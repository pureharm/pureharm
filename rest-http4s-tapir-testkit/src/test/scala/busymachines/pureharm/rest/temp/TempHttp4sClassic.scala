package busymachines.pureharm.rest.temp

object TempHttp4sClassic {
  import busymachines.pureharm.rest._
  import busymachines.pureharm.rest.implicits._

  import org.http4s.dsl.Http4sDsl
  import busymachines.pureharm.effects._
  import busymachines.pureharm.effects.implicits._

  def service[F[_]: Sync]: HttpRoutes[F] = {
    val dsl: Http4sDsl[F] = Http4sDsl[F]
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType))

      case req @ POST -> Root / path =>
        Ok {
          for {
            _      <- req.as[MyInputType]
            _      <- path.pure[F]
            result <- (??? : MyOutputType).pure[F]
          } yield result
        }

    }
  }

}
