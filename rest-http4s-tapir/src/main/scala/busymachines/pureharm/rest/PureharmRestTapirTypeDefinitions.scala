package busymachines.pureharm.rest

import busymachines.pureharm.anomaly.AnomalyBase
import sttp.tapir

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jul 2020
  */
trait PureharmRestTapirTypeDefinitions {
  type Endpoint[I, E, O, S]       = tapir.Endpoint[I, E, O, S]
  type SimpleEndpoint[I, O]       = tapir.Endpoint[I, AnomalyBase, O, Nothing]
  type StreamingEndpoint[I, O, S] = tapir.Endpoint[I, AnomalyBase, O, S]

  type TCodec[L, H, +CF <: tapir.CodecFormat] = tapir.Codec[L, H, CF]
  val TCodec: tapir.Codec.type = tapir.Codec

  /**
    * This should serve as the base input for all pureharm routes
    */
  val phEndpoint: tapir.Endpoint[Unit, AnomalyBase, Unit, Nothing] = {
    import sttp.tapir.json.circe._
    import busymachines.pureharm.internals.json.AnomalyJsonCodec.pureharmAnomalyBaseCodec
    import PureharmRestTapirImplicits.tapirSchemaAnomalies
    tapir.endpoint.errorOut(jsonBody[AnomalyBase])
  }
}
