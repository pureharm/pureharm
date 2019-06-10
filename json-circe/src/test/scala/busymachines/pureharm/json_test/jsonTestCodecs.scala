package busymachines.pureharm.json_test

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 11 Jun 2019
  *
  */
object jsonTestCodecs {
  import busymachines.pureharm.json._

  val `OutdoorMelons.Color.codec`:            Codec[OutdoorMelons.Color] = derive.codec[OutdoorMelons.Color]
  val `OutdoorMelons.Color.enumerationCodec`: Codec[OutdoorMelons.Color] = derive.enumerationCodec[OutdoorMelons.Color]

}
