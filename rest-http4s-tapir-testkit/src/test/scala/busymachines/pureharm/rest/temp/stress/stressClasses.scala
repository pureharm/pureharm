package busymachines.pureharm.rest.temp.stress

import busymachines.pureharm.rest.temp._

//---------------------------------
final case class MyInputType1(
  f1: PHString,
  f2: PHInt,
  f3: PHLong,
  f4: List[PHUUID],
  f5: Option[PHString],
)

object MyInputType1 {
  import busymachines.pureharm.json._
  import busymachines.pureharm.json.implicits._

  implicit val codec: Codec[MyInputType1] = derive.codec[MyInputType1]
}

final case class MyOutputType1(
  id: PHUUID,
  f1: PHString,
  f2: PHInt,
  f3: PHLong,
  f4: List[PHUUID],
  f5: Option[PHString],
)

object MyOutputType1 {
  import busymachines.pureharm.json._
  import busymachines.pureharm.json.implicits._
  implicit val codec: Codec[MyOutputType1] = derive.codec[MyOutputType1]
}
//---------------------------------
