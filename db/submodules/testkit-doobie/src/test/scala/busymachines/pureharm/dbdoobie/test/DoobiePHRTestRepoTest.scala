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
final class DoobiePHRTestRepoTest extends PHRTestRepoTest[Transactor[IO]] with ParallelTestExecution {
  override type FixtureParam = DoobiePHRTestRepo[IO]

  override def setup: RepoTestSetup[Transactor[IO]] = DoobiePHRTestRepoTest

  override def fixture(meta: MetaData, trans: Transactor[IO]): Resource[IO, DoobiePHRTestRepo[IO]] =
    Resource.pure[IO, DoobiePHRTestRepo[IO]](DoobiePHRTestRepo(trans))

}

object DoobiePHRTestRepoTest extends DoobieRepoTestSetup {

  override def dbConfig(meta: TestData)(implicit logger: TestLogger): DBConnectionConfig =
    PHRTestDBConfig.dbConfig.copy(
      schema = PHRTestDBConfig.schemaName(s"doobie_${meta.pos.get.lineNumber}")
    )

}
