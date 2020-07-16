package busymachines.pureharm.internals.rest

import busymachines.pureharm.anomaly._
import busymachines.pureharm.internals.json.AnomalyJsonCodec
import sttp.tapir._

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 14 Jul 2020
  */
object PureharmTapirEndpoint {
  import busymachines.pureharm.json.{Decoder, Encoder, Codec => CCodec}

  def phEndpoint: Endpoint[Unit, Throwable, Unit, Nothing] = {
    import sttp.tapir.json.circe._

    implicit val thrCC: CCodec[Throwable] = AnomalyJsonCodec.pureharmThrowableCodec

    implicit val nfcc: CCodec[NotFoundAnomaly]           = thrCC.asInstanceOf[CCodec[NotFoundAnomaly]]
    implicit val uacc: CCodec[UnauthorizedAnomaly]       = thrCC.asInstanceOf[CCodec[UnauthorizedAnomaly]]
    implicit val facc: CCodec[ForbiddenAnomaly]          = thrCC.asInstanceOf[CCodec[ForbiddenAnomaly]]
    implicit val dncc: CCodec[DeniedAnomaly]             = thrCC.asInstanceOf[CCodec[DeniedAnomaly]]
    implicit val iicc: CCodec[InvalidInputAnomaly]       = thrCC.asInstanceOf[CCodec[InvalidInputAnomaly]]
    implicit val cfcc: CCodec[ConflictAnomaly]           = thrCC.asInstanceOf[CCodec[ConflictAnomaly]]
    implicit val ascc: CCodec[Anomalies]                 = thrCC.asInstanceOf[CCodec[Anomalies]]
    implicit val nicc: CCodec[NotImplementedCatastrophe] = thrCC.asInstanceOf[CCodec[NotImplementedCatastrophe]]
    implicit val ctcc: CCodec[Catastrophe]               = thrCC.asInstanceOf[CCodec[Catastrophe]]

    implicit val thrSc: Schema[Throwable]                 = PureharmTapirSchemas.tapirSchemaAnomalies
    implicit val nfsc:  Schema[NotFoundAnomaly]           = thrSc.asInstanceOf[Schema[NotFoundAnomaly]]
    implicit val uasc:  Schema[UnauthorizedAnomaly]       = thrSc.asInstanceOf[Schema[UnauthorizedAnomaly]]
    implicit val fasc:  Schema[ForbiddenAnomaly]          = thrSc.asInstanceOf[Schema[ForbiddenAnomaly]]
    implicit val dnsc:  Schema[DeniedAnomaly]             = thrSc.asInstanceOf[Schema[DeniedAnomaly]]
    implicit val iisc:  Schema[InvalidInputAnomaly]       = thrSc.asInstanceOf[Schema[InvalidInputAnomaly]]
    implicit val cfsc:  Schema[ConflictAnomaly]           = thrSc.asInstanceOf[Schema[ConflictAnomaly]]
    implicit val assc:  Schema[Anomalies]                 = thrSc.asInstanceOf[Schema[Anomalies]]
    implicit val nisc:  Schema[NotImplementedCatastrophe] = thrSc.asInstanceOf[Schema[NotImplementedCatastrophe]]
    implicit val ctsc:  Schema[Catastrophe]               = thrSc.asInstanceOf[Schema[Catastrophe]]

    import sttp.model.StatusCode
    endpoint.errorOut(
      oneOf[Throwable](
        statusMapping(StatusCode.NotFound, jsonBody[NotFoundAnomaly].description("not found")),
        statusMapping(StatusCode.Unauthorized, jsonBody[UnauthorizedAnomaly].description("unauthorized")),
        statusMapping(StatusCode.Forbidden, jsonBody[ForbiddenAnomaly].description("forbidden")),
        statusMapping(StatusCode.Forbidden, jsonBody[DeniedAnomaly].description("access denied")),
        statusMapping(StatusCode.BadRequest, jsonBody[InvalidInputAnomaly].description("invalid input")),
        statusMapping(StatusCode.Conflict, jsonBody[ConflictAnomaly].description("conflicting value")),
        statusMapping(StatusCode.BadRequest, jsonBody[Anomalies].description("invalid input, multiple validation")),
        statusMapping(StatusCode.NotImplemented, jsonBody[NotImplementedCatastrophe].description("not implemented")),
        statusMapping(StatusCode.InternalServerError, jsonBody[Catastrophe].description("internal server error")),
        statusMapping(StatusCode.InternalServerError, jsonBody[Throwable].description("internal server error")),
      )
    )
  }

}
