package busymachines.pureharm.dbdoobie.test

import busymachines.pureharm.db._
import busymachines.pureharm.db.testdata._
import busymachines.pureharm.dbdoobie._
import busymachines.pureharm.db.testkit._
import busymachines.pureharm.dbdoobie.testkit._
import busymachines.pureharm.effects._
import busymachines.pureharm.testkit._
import org.scalatest._

final class DoobieCompositeTest extends DBTest[Transactor[IO]] {
  override type ResourceType = (DoobiePHRowRepo[IO], DoobieExtPHRowRepo[IO])

  override def setup: DBTestSetup[Transactor[IO]] = DoobieCompositeTest

  override def resource(meta: MetaData, trans: Transactor[IO]): Resource[IO, ResourceType] =
    Resource.pure[IO, ResourceType]((DoobiePHRowRepo(trans), DoobieExtPHRowRepo(trans)))

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

object DoobieCompositeTest extends DoobieDBTestSetup {

  override def dbConfig(meta: TestData)(implicit logger: TestLogger): DBConnectionConfig =
    PHRTestDBConfig.dbConfig.withSchemaFromClassAndTest(prefix = "doobie", meta = meta)
}
