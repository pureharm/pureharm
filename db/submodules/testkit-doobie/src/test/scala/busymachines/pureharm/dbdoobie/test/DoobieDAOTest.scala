package busymachines.pureharm.dbdoobie.test

import busymachines.pureharm.db._
import busymachines.pureharm.db.test._
import busymachines.pureharm.db.testkit._
import busymachines.pureharm.dbdoobie._
import busymachines.pureharm.dbdoobie.testkit._
import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._
import busymachines.pureharm.testkit._
import org.scalatest._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 25 Jun 2020
  *
  */
final class DoobieDAOTest extends PureharmRowTest[Transactor[IO]] with ParallelTestExecution {
  import runtime._
  override type FixtureParam = DoobiePureharmRowDAO[IO]

  override def setup: PureharmDAOTestSetup[Transactor[IO]] = DoobieDAOTest

  override def fixture(meta: MetaData, trans: Transactor[IO]): Resource[IO, DoobiePureharmRowDAO[IO]] =
    Resource.pure[IO, DoobiePureharmRowDAO[IO]](DoobiePureharmRowDAO(trans))

}

object DoobieDAOTest extends PureharmTestSetupDoobie {

  override def dbConfig(meta: TestData)(implicit logger: TestLogger): DBConnectionConfig =
    DBTestConfig.dbConfig.copy(
      schema = DBTestConfig.schemaName(s"doobie_${meta.pos.get.lineNumber}")
    )

}
