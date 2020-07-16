package busymachines.pureharm.internals.rest

import sttp.tapir.generic.internal.SchemaMagnoliaDerivation

/**
  * Mirrors: [[sttp.tapir.TapirAliases]] but it realiases Codec to TapirCodec
  * because that conflicts with Circe's Codec, and that's way too annoying.
  */
trait PureharmTapirAliases {

  type SimpleEndpoint[I, O]        = sttp.tapir.Endpoint[I, Throwable, O, Nothing]
  type StreamingEndpoint[I, O, +S] = sttp.tapir.Endpoint[I, Throwable, O, S]

  /**
    * This should serve as your basis for most endpoints in your app.
    * It provides grade A interpretation of all Anomaly types, plus
    * the good old mapping to status codes. You can easily glance the
    * mapping from the implementation. But here it is for easy glancing:
    *
    * {{{
    *   NotFoundAnomaly              StatusCode.NotFound
    *   UnauthorizedAnomaly          StatusCode.Unauthorized
    *   ForbiddenAnomaly             StatusCode.Forbidden
    *   DeniedAnomaly                StatusCode.Forbidden
    *   InvalidInputAnomaly          StatusCode.BadRequest
    *   ConflictAnomaly              StatusCode.Conflict
    *   Anomalies                    StatusCode.BadRequest
    *   NotImplementedCatastrophe    StatusCode.NotImplemented
    *   Catastrophe                  StatusCode.InternalServerError
    *   Throwable                    StatusCode.InternalServerError
    * }}}
    */
  val phEndpoint: SimpleEndpoint[Unit, Unit] = PureharmTapirEndpoint.phEndpoint

  //mirroring starts here:

  type CodecFormat = sttp.tapir.CodecFormat
  val CodecFormat: sttp.tapir.CodecFormat.type = sttp.tapir.CodecFormat

  /** Codec.scala */
  type TapirCodec[L, H, CF <: CodecFormat] = sttp.tapir.Codec[L, H, CF]
  val TapirCodec: sttp.tapir.Codec.type = sttp.tapir.Codec

  /** DecodeResult.scala */
  type DecodeResult[+T] = sttp.tapir.DecodeResult[T]
  val DecodeResult: sttp.tapir.DecodeResult.type = sttp.tapir.DecodeResult

  /** Defaults.scala */
  val Defaults: sttp.tapir.Defaults.type = sttp.tapir.Defaults

  /** Endpoint.scala */
  type Endpoint[I, E, O, +S] = sttp.tapir.Endpoint[I, E, O, S]
  val Endpoint: sttp.tapir.Endpoint.type = sttp.tapir.Endpoint

  type EndpointInfo = sttp.tapir.EndpointInfo
  val EndpointInfo: sttp.tapir.EndpointInfo.type = sttp.tapir.EndpointInfo

  /** EndpointIO.scala */
  type EndpointInput[I] = sttp.tapir.EndpointInput[I]
  val EndpointInput: sttp.tapir.EndpointInput.type = sttp.tapir.EndpointInput

  type EndpointOutput[O] = sttp.tapir.EndpointOutput[O]
  val EndpointOutput: sttp.tapir.EndpointOutput.type = sttp.tapir.EndpointOutput

  type EndpointIO[I] = sttp.tapir.EndpointIO[I]
  val EndpointIO: sttp.tapir.EndpointIO.type = sttp.tapir.EndpointIO

  type StreamingEndpointIO[I, +S] = sttp.tapir.StreamingEndpointIO[I, S]
  val StreamingEndpointIO: sttp.tapir.StreamingEndpointIO.type = sttp.tapir.StreamingEndpointIO

  /** package.scala */
  type RawPart           = sttp.tapir.RawPart
  type AnyPart           = sttp.tapir.AnyPart
  type AnyListCodec      = sttp.tapir.AnyListCodec
  type MultipartCodec[T] = sttp.tapir.MultipartCodec[T]

  /** RenderPathTemplate.scala */
  val RenderPathTemplate: sttp.tapir.RenderPathTemplate.type = sttp.tapir.RenderPathTemplate

  /** SchemaType.scala */
  type SchemaType = sttp.tapir.SchemaType
  val SchemaType: sttp.tapir.SchemaType.type = sttp.tapir.SchemaType

  /** Schema.scala */
  type Schema[T] = sttp.tapir.Schema[T]
  val Schema: sttp.tapir.Schema.type with SchemaMagnoliaDerivation = sttp.tapir.Schema

  /** Tapir.scala */
  type Tapir              = sttp.tapir.Tapir
  type TapirDerivedInputs = sttp.tapir.TapirDerivedInputs

  /** TapirAuth.scala */
  val TapirAuth: sttp.tapir.TapirAuth.type = sttp.tapir.TapirAuth
}
