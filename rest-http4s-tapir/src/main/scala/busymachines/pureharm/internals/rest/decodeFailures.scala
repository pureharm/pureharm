package busymachines.pureharm.internals.rest

import io.circe.{CursorOp, DecodingFailure}
import busymachines.pureharm.anomaly._

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 15 Jul 2020
  */

final private[pureharm] case class CirceDecodingAnomaly(
  failure: io.circe.DecodingFailure
) extends InvalidInputAnomaly(
    "Failed to decode JSON. See 'parameters.path' for a history of decoding failure.",
    Option(failure),
  ) {

  override val id: AnomalyID = CirceDecodingAnomaly.CirceDecodingAnomalyID

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
  case object CirceDecodingAnomalyID extends AnomalyID { override val name: String = "rest_json_0" }
}

//--------------------------------------------
final private[pureharm] case class CirceParsingAnomaly(
  failure: io.circe.ParsingFailure
) extends InvalidInputAnomaly(
    "Failed to parse JSON. Double check your syntax",
    Option(failure),
  ) {
  override val id: AnomalyID = CirceParsingAnomaly.CirceParsingAnomalyID
}

object CirceParsingAnomaly {
  case object CirceParsingAnomalyID extends AnomalyID { override val name: String = "rest_json_1" }
}
