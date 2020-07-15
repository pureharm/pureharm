package busymachines.pureharm.rest.temp

import busymachines.pureharm.json._
import busymachines.pureharm.json.implicits._

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jul 2020
  */
final case class MyInputType(
  f1: PHString,
  f2: PHInt,
  f3: PHLong,
  fl: List[PHLong],
  f4: List[PHUUID],
  f5: Option[PHString],
)

object MyInputType {
  implicit val codec: Codec[MyInputType] = derive.codec[MyInputType]
}

final case class MyOutputType(
  id: PHUUID,
  f1: PHString,
  f2: PHInt,
  f3: PHLong,
  fl: List[PHLong],
  f4: List[PHUUID],
  f5: Option[PHString],
)

object MyOutputType {
  implicit val codec: Codec[MyOutputType] = derive.codec[MyOutputType]
}
