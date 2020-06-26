package busymachines.pureharm.dbdoobie.test

import busymachines.pureharm.db._
import busymachines.pureharm.db.testdata._
import busymachines.pureharm.db.testkit._
import busymachines.pureharm.dbdoobie._
import busymachines.pureharm.dbdoobie.testkit._
import busymachines.pureharm.effects._
import busymachines.pureharm.testkit._
import org.scalatest._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 25 Jun 2020
  *
  */
final class DoobiePHRowDAOTest extends PHTRowDAOTest[Transactor[IO]] with ParallelTestExecution {
  override type FixtureParam = DoobiePHRowDAO[IO]

  override def setup: PureharmDAOTestSetup[Transactor[IO]] = DoobiePHRowDAOTest

  override def fixture(meta: MetaData, trans: Transactor[IO]): Resource[IO, DoobiePHRowDAO[IO]] =
    Resource.pure[IO, DoobiePHRowDAO[IO]](DoobiePHRowDAO(trans))

}

object DoobiePHRowDAOTest extends PHTestSetupDoobie {

  override def dbConfig(meta: TestData)(implicit logger: TestLogger): DBConnectionConfig =
    PHTDBConfig.dbConfig.copy(
      schema = PHTDBConfig.schemaName(s"doobie_${meta.pos.get.lineNumber}")
    )

}
