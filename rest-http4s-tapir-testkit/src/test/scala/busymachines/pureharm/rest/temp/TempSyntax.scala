package busymachines.pureharm.rest.temp

import busymachines.pureharm.rest._
import busymachines.pureharm.rest.implicits._

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jul 2020
  */
object TempSyntax {
  import sttp.tapir._

  case class PathInputSample(
    s: PHString,
    u: PHUUID,
    l: PHLong,
    i: PHInt,
  )

  def testPhantomParams: SimpleEndpoint[(PathInputSample, PHString, PHUUID), Unit] =
    phEndpoint.get
      .in(
        ("test" /
          path[PHString]("stringPath") /
          path[PHUUID]("uuidPath") /
          path[PHLong]("longPath") /
          path[PHInt]("intPath")).mapTo(PathInputSample)
      )
      .in(query[PHString]("stringQ").description("just a string query param"))
      .in(query[PHUUID]("uuidQ").description("just an uuid query param"))
}
