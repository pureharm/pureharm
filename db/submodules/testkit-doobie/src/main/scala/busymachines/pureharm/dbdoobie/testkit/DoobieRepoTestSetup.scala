package busymachines.pureharm.dbdoobie.testkit

import busymachines.pureharm.db._
import busymachines.pureharm.db.testkit._
import busymachines.pureharm.dbdoobie._
import busymachines.pureharm.dbdoobie.implicits._
import busymachines.pureharm.effects._
import busymachines.pureharm.testkit._
import org.scalatest._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 26 Jun 2020
  *
  */
trait DoobieRepoTestSetup extends RepoTestSetup[Transactor[IO]] {

  /**
    * Should be overridden to create a connection config appropriate for the test
    */
  override def dbConfig(meta: TestData)(implicit logger: TestLogger): DBConnectionConfig

  override def dbTransactorInstance(
    meta:        TestData
  )(implicit rt: PureharmTestRuntime, logger: TestLogger): Resource[IO, Transactor[IO]] = {
    val config = dbConfig(meta)
    import rt.contextShift
    for {
      _     <- logger.info(MDCKeys(meta))(s"CREATING Transactor[IO] for: ${config.jdbcURL}").to[Resource[IO, *]]
      trans <- Transactor.pureharmTransactor[IO](
        dbConfig  = dbConfig(meta),
        dbConnEC  = DoobieConnectionEC.safe(rt.executionContextFT),
        dbBlocker = DoobieBlocker(rt.blocker),
      )

    } yield trans
  }
}