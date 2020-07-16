package busymachines.pureharm.internals.rest

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 16 Jul 2020
  */
object TapirOps {
  import sttp.tapir._

  final class AuthOps(val o: TapirAuth.type) extends AnyVal {

    def xCustomAuthHeader[T: Codec[String, *, CodecFormat.TextPlain]](
      headerName: String
    ): EndpointInput.Auth.Http[T] = {
      val codec     = implicitly[Codec[List[String], T, CodecFormat.TextPlain]]
      val authCodec = Codec.list(Codec.string).map(codec).schema(codec.schema)
      EndpointInput.Auth.Http(
        "Custom",
        header[T](headerName)(authCodec).description(
          s"Authentication done with token required in header: $headerName"
        ),
      )
    }

  }

}
