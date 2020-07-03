package busymachines.pureharm.db.psql

import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._
import busymachines.pureharm.db._

import org.postgresql.util._

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 02 Jul 2020
  */
object PSQLExceptionInterpreters {

  object PSQLStates {
    val UniqueViolation: String = PSQLState.UNIQUE_VIOLATION.getState
  }

  object PSQLErrorParsers {
    import atto._, Atto._
    private val underscore = char('_')
    private val column: Parser[String] = many1(letterOrDigit | underscore).map(_.mkString_(""))
    private val values: Parser[String] = many1(anyChar).map(_.mkString_(""))

    /**
      * usually has the value of format:
      * {{{Key (id)=(row1_id) already exists.}}}
      */
    object unique {

      val parser: Parser[(String, String)] = for {
        _          <- string("Key (")
        columnName <- column
        _          <- string(")=(")
        //the parser here is hard to write since it would require look-ahead
        //to the ') already exists.' at the end in order to parse all
        //possible postgresql values... and that's difficult. So this hack
        //will work just fine :)
        value      <- values.map(_.stripSuffix(") already exists."))
      } yield (columnName, value)

      def apply[F[_]: MonadAttempt](s: String): F[(String, String)] =
        parser.parse(s).done.either.leftMap(s => new RuntimeException(s): Throwable).liftTo[F]

    }

  }

  /**
    * Only call when [[PSQLException#getSQLState]] == [[PSQLStates.UniqueViolation]]
    *
    * Will attempt to extract the values of the state by doing regex over the
    * error message... yey, java?
    */
  def uniqueKey(e: PSQLException): Attempt[DBUniqueConstraintViolationAnomaly] =
    PSQLExceptionInterpreters.PSQLErrorParsers
      .unique[Attempt](e.getServerErrorMessage.getDetail)
      .map(t => DBUniqueConstraintViolationAnomaly(t._1, t._2))

  lazy val adapt: PartialFunction[Throwable, Throwable] = {
    case e: PSQLException =>
      e.getSQLState match {
        case PSQLStates.UniqueViolation => uniqueKey(e).getOrElse(e)
        case _                          => e
      }
    case e => e
  }
}
