package busymachines.pureharm.internals.rest

import busymachines.pureharm.anomaly.{Anomaly, AnomalyBase}
import sttp.model.StatusCode
import sttp.tapir.DecodeResult.InvalidValue
import sttp.tapir._
import sttp.tapir.server.{DecodeFailureContext, DecodeFailureHandler, DecodeFailureHandling}

import scala.annotation.tailrec

object PureharmServerDefaults {

//  import sttp.tapir._
//  import sttp.tapir.server._
//  import sttp.tapir.json.circe._
//  import busymachines.pureharm.rest.PureharmRestTapirImplicits._
//
//  import busymachines.pureharm.internals.json.AnomalyJsonCodec.pureharmAnomalyBaseCodec
//
//  def myFailureResponse(statusCode: StatusCode, message: String): DecodeFailureHandling =
//    DecodeFailureHandling.response(jsonBody[AnomalyBase].and(statusCode))(
//      (Anomaly(message), statusCode)
//    )
//
//  val myDecodeFailureHandler = ServerDefaults.decodeFailureHandler.copy(
//    response = myFailureResponse
//  )
}
