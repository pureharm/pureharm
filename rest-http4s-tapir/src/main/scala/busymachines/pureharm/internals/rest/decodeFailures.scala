package busymachines.pureharm.internals.rest

import io.circe.{CursorOp, DecodingFailure}
import busymachines.pureharm.anomaly._

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 15 Jul 2020
  */

final private[pureharm] case class CirceDecodingAnomaly(
  failure: DecodingFailure
) extends InvalidInputAnomaly(
    "Failed to decode JSON. See 'parameters.path' for a history of decoding failure.",
    Option(failure),
  ) {

  override val id: AnomalyID = CirceDecodingAnomaly.ID

  override val parameters: Anomaly.Parameters = {
    val reverseHistory = failure.history.reverse
    Anomaly.Parameters(
      "expectedType" -> failure.message,
      "path"         -> (reverseHistory.map(lensOpToString) ++
        countArrayFailureIndex(failure.history).map(idx => s"failed to decode array element index [$idx]").toList),
    ) ++ super.parameters
  }

  /**
    * For instance, for a list of 4 elements, we expect to see such a history:
    * {{{
    *   List(MoveRight, MoveRight, MoveRight, DownArray, DownField(f4))
    * }}}
    * So we count all contiguous MoveRight from head to tail.
    *   - Since going down into nested arrays requires a DownArray
    * TODO: test to see if this heuristic works properly for:
    *  - arrays in arrays
    *  - arrays containing objects containing arrays (and failure is in these nested arrays)
    *  - previous top level fields were arrays, and were all right
    * @return
    */
  private def countArrayFailureIndex(o: List[CursorOp]): Option[Int] = {
    val rightMoves = o.takeWhile(_.isInstanceOf[CursorOp.MoveRight.type])
    if (rightMoves.isEmpty)
      Option.empty
    else Option(rightMoves.size)
  }

  private def lensOpToString(o: CursorOp): String =
    o match {
      case s: CursorOp.Field     => s"field: ${s.k}"
      case s: CursorOp.DownField => s"decoding field: ${s.k}"
      case CursorOp.DownArray => s"decoding array"
      case CursorOp.MoveRight => s"decoding array element"
      case s                  => s.toString
    }
}

object CirceDecodingAnomaly {
  case object ID extends AnomalyID { override val name: String = "rest_json_0" }
}

//--------------------------------------------
final private[pureharm] case class CirceParsingAnomaly(
  failure: io.circe.ParsingFailure
) extends InvalidInputAnomaly(
    "Failed to parse JSON. Double check your syntax",
    Option(failure),
  ) {
  override val id: AnomalyID = CirceParsingAnomaly.ID
}

object CirceParsingAnomaly {
  case object ID extends AnomalyID { override val name: String = "rest_json_1" }
}

//--------------------------------------------
abstract private[pureharm] class MissingRequestPart(
  tpe:          String,
  tapirMessage: String,
) extends InvalidInputAnomaly(
    s"Missing required part: '$tpe'"
  ) {
  override val id: AnomalyID = MissingRequestPart.ID

  override def parameters: Anomaly.Parameters = super.parameters ++ Anomaly.Parameters(
    "diagnostic" -> tapirMessage
  )
}

object MissingRequestPart {
  case object ID extends AnomalyID { override val name: String = "rest_missing" }
}

//--------------------------------------------
final private[pureharm] case class MissingRequestPartGeneric(
  tpe:          String,
  tapirMessage: String,
) extends InvalidInputAnomaly(
    s"Missing required: $tpe >> $tapirMessage"
  ) {
  override val id: AnomalyID = MissingRequestPartGeneric.ID

  override def parameters: Anomaly.Parameters = super.parameters ++ Anomaly.Parameters(
    "diagnostic" -> tapirMessage
  )
}

object MissingRequestPartGeneric {
  case object ID extends AnomalyID { override val name: String = "rest_missing" }
}

//--------------------------------------------
final private[pureharm] case class MissingQueryParam(
  paramName:    String,
  tapirMessage: String,
) extends MissingRequestPart("Query parameter", tapirMessage) {
  override val id: AnomalyID = MissingQueryParam.ID

  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "missingParam" -> paramName
  ) ++ super.parameters
}

object MissingQueryParam {
  case object ID extends AnomalyID { override val name: String = "rest_missing_qp" }
}

//--------------------------------------------
final private[pureharm] case class MissingHeader(
  headerName:   String,
  tapirMessage: String,
) extends MissingRequestPart("Header", tapirMessage) {
  override val id: AnomalyID = MissingHeader.ID

  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "missingHeader" -> headerName
  ) ++ super.parameters
}

object MissingHeader {
  case object ID extends AnomalyID { override val name: String = "rest_missing_header" }
}

//--------------------------------------------
final private[pureharm] case class InvalidMultiple(
  tapirMessage: String
) extends InvalidInputAnomaly(tapirMessage) {
  override val id: AnomalyID = InvalidMultiple.ID

}

object InvalidMultiple {
  case object ID extends AnomalyID { override val name: String = "rest_invalid_multiple" }
}

//--------------------------------------------
abstract private[pureharm] class InvalidRequestPart(
  tpe:          String,
  tapirMessage: String,
  error:        String,
) extends InvalidInputAnomaly(
    s"Request part: '$tpe' is present, but has an invalid value. Please check that it's of the proper type, e.g. a number, UUID, proper date format, etc."
  ) {
  override val id: AnomalyID = MissingRequestPart.ID

  override def parameters: Anomaly.Parameters = super.parameters ++ Anomaly.Parameters(
    "partType"   -> tpe,
    "diagnostic" -> tapirMessage,
    "error"      -> error,
  )
}

object InvalidRequestPart {
  case object ID extends AnomalyID { override val name: String = "rest_invalid" }
}

//--------------------------------------------
final private[pureharm] case class InvalidRequestPartGeneric(
  tpe:          String,
  givenValue:   String,
  tapirMessage: String,
  error:        String,
) extends InvalidRequestPart(
    tpe          = tpe,
    tapirMessage = tapirMessage,
    error        = error,
  ) {
  override val id: AnomalyID = InvalidRequestPartGeneric.ID

  override def parameters: Anomaly.Parameters = super.parameters ++ Anomaly.Parameters(
    "givenValue" -> givenValue
  )
}

object InvalidRequestPartGeneric {
  case object ID extends AnomalyID { override val name: String = "rest_invalid" }
}

//--------------------------------------------
final private[pureharm] case class InvalidQueryParam(
  paramName:    String,
  givenValue:   String,
  tapirMessage: String,
  error:        String,
) extends InvalidRequestPart(
    tpe          = "Query parameter",
    tapirMessage = tapirMessage,
    error        = error,
  ) {
  override val id: AnomalyID = InvalidQueryParam.ID

  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "paramName"  -> paramName,
    "givenValue" -> givenValue,
  ) ++ super.parameters
}

object InvalidQueryParam {
  case object ID extends AnomalyID { override val name: String = "rest_invalid_qp" }
}

//--------------------------------------------
final private[pureharm] case class InvalidHeader(
  headerName:   String,
  givenValue:   String,
  tapirMessage: String,
  error:        String,
) extends InvalidRequestPart(
    tpe          = "Header",
    tapirMessage = tapirMessage,
    error        = error,
  ) {
  override val id: AnomalyID = InvalidHeader.ID

  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "headerName" -> headerName,
    "givenValue" -> givenValue,
  ) ++ super.parameters
}

object InvalidHeader {
  case object ID extends AnomalyID { override val name: String = "rest_invalid_hd" }
}

//--------------------------------------------
final private[pureharm] case class MissingAuthHeader(
  headerName: String
) extends UnauthorizedAnomaly(
    s"Missing required header used for authentication: $headerName"
  ) {
  override val id: AnomalyID = MissingAuthHeader.ID

  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "headerName" -> headerName
  ) ++ super.parameters
}

object MissingAuthHeader {
  case object ID extends AnomalyID { override val name: String = "auth_missing_header" }
}

//--------------------------------------------
final private[pureharm] case class InvalidAuthHeader(
  headerName: String,
  givenValue: String,
  error:      String,
) extends UnauthorizedAnomaly(
    s"Header value used for authentication: $headerName >> has incorect syntax/format. Please make sure it's of the proper type or format."
  ) {
  override val id: AnomalyID = InvalidAuthHeader.ID

  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "headerName" -> headerName,
    "givenValue" -> givenValue,
    "error"      -> error,
  ) ++ super.parameters
}

object InvalidAuthHeader {
  case object ID extends AnomalyID { override val name: String = "auth_invalid_header" }
}

//--------------------------------------------
final private[pureharm] case class MismatchAnomaly(
  tpe:          String,
  expected:     String,
  given:        String,
  tapirMessage: String,
) extends InvalidInputAnomaly(
    s"Mismatched input of type '$tpe' >> expected=$expected, given=$given."
  ) {
  override val id: AnomalyID = MismatchAnomaly.ID

  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "type"       -> tpe,
    "expected"   -> expected,
    "given"      -> given,
    "diagnostic" -> tapirMessage,
  ) ++ super.parameters
}

object MismatchAnomaly {
  case object ID extends AnomalyID { override val name: String = "rest_mismatch" }
}

//--------------------------------------------
final private[pureharm] case class ValidationAnomaly(
  tapirMessage: String,
  errors:       List[String],
) extends InvalidInputAnomaly(
    s"Invalid input"
  ) {
  override val id: AnomalyID = ValidationAnomaly.ID

  override val parameters: Anomaly.Parameters = Anomaly.Parameters(
    "diagnostic" -> tapirMessage,
    "errors"     -> errors,
  ) ++ super.parameters
}

object ValidationAnomaly {
  case object ID extends AnomalyID { override val name: String = "rest_validation" }
}
//--------------------------------------------
