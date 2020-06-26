package busymachines.pureharm.db.testkit

import busymachines.pureharm.db._
import busymachines.pureharm.db.flyway.FlywayConfig
import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._
import busymachines.pureharm.testkit._
import org.scalatest.TestData

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 25 Jun 2020
  *
  */
trait PureharmDAOTestSetup[DBTransactor] {

  /**
    * Should be overridden to create a connection config appropriate for the test
    */
  def dbConfig(meta: TestData)(implicit logger: TestLogger): DBConnectionConfig

  //hack to remove unused param error
  def flywayConfig(meta: TestData): Option[FlywayConfig] = Option(meta) >> Option.empty

  protected def dbTransactorInstance(
    meta:        TestData
  )(implicit rt: PureharmTestRuntime, logger: TestLogger): Resource[IO, DBTransactor]

  def transactor(
    meta:        TestData
  )(implicit rt: PureharmTestRuntime, logger: TestLogger): Resource[IO, DBTransactor] =
    for {
      _       <- logger.info(MDCKeys(meta))("SETUP — init").to[Resource[IO, *]]
      _       <- _cleanDB(meta)
      _       <- _initDB(meta)
      fixture <- dbTransactorInstance(meta)
    } yield fixture

  protected def _initDB(meta: TestData)(implicit rt: PureharmTestRuntime, logger: TestLogger): Resource[IO, Unit] =
    for {
      _ <- logger.info(MDCKeys(meta))("SETUP — preparing DB").to[Resource[IO, *]]
      _ <- flyway.Flyway.migrate[IO](dbConfig = dbConfig(meta), flywayConfig(meta)).to[Resource[IO, *]]
      _ <- logger.info(MDCKeys(meta))("SETUP — done preparing DB").to[Resource[IO, *]]
    } yield ()

  protected def _cleanDB(meta: TestData)(implicit rt: PureharmTestRuntime, logger: TestLogger): Resource[IO, Unit] =
    for {
      _ <- logger.info(MDCKeys(meta))("SETUP — cleaning DB for a clean slate").to[Resource[IO, *]]
      _ <- flyway.Flyway.clean[IO](dbConfig(meta)).to[Resource[IO, *]]
      _ <- logger.info(MDCKeys(meta))("SETUP — done cleaning DB").to[Resource[IO, *]]
    } yield ()
}
