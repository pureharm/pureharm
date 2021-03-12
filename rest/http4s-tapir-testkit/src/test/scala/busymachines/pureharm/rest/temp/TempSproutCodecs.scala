package busymachines.pureharm.rest.temp

//Unfortunately, providing implicitly such codecs can sometimes degrade compilation by one order of magnitude.
//so it's better to be conservative, and just define them explicitly
object TempSproutCodecs {
  import busymachines.pureharm.rest._

  implicit val safePhuuidThrCodec: TapirPlainCodec[SafePHUUIDThr] = TapirCodec.uuid.sproutRefined[SafePHUUIDThr]
  implicit val phuuidCodec:        TapirPlainCodec[PHUUID]        = TapirCodec.uuid.sprout[PHUUID]
  implicit val myAuthTokenCodec:   TapirPlainCodec[MyAuthToken]   = TapirCodec.long.sprout[MyAuthToken]
  implicit val PHLongCodec:        TapirPlainCodec[PHLong]        = TapirCodec.long.sprout[PHLong]
  implicit val PHHeaderCodec:      TapirPlainCodec[PHHeader]      = TapirCodec.long.sprout[PHHeader]
  implicit val PHIntCodec:         TapirPlainCodec[PHInt]         = TapirCodec.int.sprout[PHInt]

}
