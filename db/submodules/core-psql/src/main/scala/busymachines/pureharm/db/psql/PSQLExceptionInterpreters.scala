package busymachines.pureharm.db.psql

import busymachines.pureharm.anomaly._
import busymachines.pureharm.db._
import org.postgresql.util._

import scala.util.matching.Regex

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 02 Jul 2020
  */
object PSQLExceptionInterpreters {

  object PSQLStates {
    val UniqueViolation: String = PSQLState.UNIQUE_VIOLATION.getState
  }

  object PSQLErrorParsers {}

  /**
    * Only call when [[PSQLException#getSQLState]] == [[PSQLStates.UniqueViolation]]
    *
    * Will attempt to extract the values of the state by doing regex over the
    * error message... yey, java?
    */
  def uniqueKey(e: PSQLException): DBUniqueConstraintViolationAnomaly =
    /**
      * usually has the value of format:
      * {{{Key (id)=(row1_id) already exists}}}
      */
    ???

  lazy val adapt: PartialFunction[Throwable, Throwable] = {
    case e: PSQLException =>
      e.getSQLState match {
        case PSQLStates.UniqueViolation => uniqueKey(e)
        case _                          => NotImplementedCatastrophe(s"TODO: implement PSQLExceptionState: ${e.getSQLState}. For: $e", e)
      }
    case e => NotImplementedCatastrophe(s"TODO: implement case non-psql exception: $e", e)
  }
}
