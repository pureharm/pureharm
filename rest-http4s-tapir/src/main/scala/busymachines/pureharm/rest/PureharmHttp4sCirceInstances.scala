package busymachines.pureharm.rest

import fs2.Chunk
import io.circe.Printer
import org.http4s._
import org.http4s.circe.CirceInstances
import org.http4s.headers.`Content-Type`
import busymachines.pureharm.effects._
import busymachines.pureharm.json._

/**
  * You need to have this in scope if you want "seamless" serializing/deserializing
  * to/from JSON in your HttpRoutes endpoints.
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 26 Jun 2018
  */
trait PureharmHttp4sCirceInstances {

  import PureharmHttp4sCirceInstances._

  /**
    * This code was copied from [[org.http4s.circe.CirceInstances#jsonEncoderWithPrinter]]
    * Ideally, we would have done directly:
    * {{{
    *   circeInstance.jsonEncoderOf[F, T]
    * }}}
    * But that throws us into an infinite loop because the implicit picks itself up.
    *
    * @return
    */
  implicit def applicativeEntityJsonEncoder[F[_]: Applicative, T: Encoder]: EntityEncoder[F, T] =
    EntityEncoder[F, Chunk[Byte]]
      .contramap[Json] { json =>
        val bytes = printer.printToByteBuffer(json)
        Chunk.byteBuffer(bytes)
      }
      .withContentType(`Content-Type`(MediaType.application.json))
      .contramap(t => Encoder[T].apply(t))

  implicit def syncEntityJsonDecoder[F[_]: Sync, T: Decoder]: EntityDecoder[F, T] =
    circeInstances.jsonOf[F, T] //TODO: adaptErrorMessage

}

object PureharmHttp4sCirceInstances {
  private val printer:        Printer        = Printer.noSpaces.copy(dropNullValues = true)
  private val circeInstances: CirceInstances = CirceInstances.withPrinter(printer).build
}
