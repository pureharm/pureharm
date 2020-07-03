package busymachines.pureharm.dbslick.test

import testdb._
import busymachines.pureharm.db._
import busymachines.pureharm.db.testdata._
import busymachines.pureharm.db.testkit._
import busymachines.pureharm.dbslick.testkit._
import busymachines.pureharm.effects._
import busymachines.pureharm.testkit.TestLogger
import org.scalatest.TestData

final class SlickCompositeTest extends DBTest[Transactor[IO]] {
  override type ResourceType = (SlickPHRowRepo[IO], SlickExtPHRowRepo[IO])

  override def setup: DBTestSetup[Transactor[IO]] = SlickCompositeTest

  override def resource(meta: MetaData, trans: Transactor[IO]): Resource[IO, ResourceType] =
    Resource.pure[IO, ResourceType] {
      implicit val t:  Transactor[IO] = trans
      implicit val ec: ConnectionIOEC = ConnectionIOEC(runtime.executionContextCT)
      (SlickPHRowRepo[IO], SlickExtPHRowRepo[IO])
    }

  private val data = PHRowRepoTest.pureharmRows

  test("insert - row1 + ext1") {
    case (row, ext) =>
      for {
        _ <- row.insert(data.row1)
        _ <- ext.insert(data.ext1)
      } yield succeed
  }

  test("insert ext1 -> key does not exist") {
    case (_, ext) =>
      for {
        _ <- ext.insert(data.extNoFPK)
      } yield succeed
  }
}

private[test] object SlickCompositeTest extends SlickDBTestSetup(testdb.jdbcProfileAPI) {

  override def dbConfig(meta: TestData)(implicit logger: TestLogger): DBConnectionConfig =
    PHRTestDBConfig.dbConfig.withSchemaFromClassAndTest(prefix = "slick", meta = meta)

}
