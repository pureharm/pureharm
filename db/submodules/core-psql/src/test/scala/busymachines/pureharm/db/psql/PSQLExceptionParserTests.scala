package busymachines.pureharm.db.psql

import busymachines.pureharm.effects._
import busymachines.pureharm.testkit.PureharmTest

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 02 Jul 2020
  */
final class PSQLExceptionParserTests extends PureharmTest {

  private val parsers = PSQLExceptionInterpreters.PSQLErrorParsers

  test("unique") {
    for {
      attempt <- parsers.unique[IO]("Key (id)=(row1_id) already exists.").attempt
    } yield assertSuccess(attempt)(("id", "row1_id"))
  }

  test("foreign") {
    for {
      attempt <-
        parsers.foreignKey[IO]("""Key (row_id)=(120-3921-039213) is not present in table "pureharm_rows".""").attempt
    } yield assertSuccess(attempt)(("row_id", "120-3921-039213", "pureharm_rows"))
  }
}
