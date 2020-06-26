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

  import io.chrisdavenport.log4cats._

  private val report: SelfAwareStructuredLogger[IO] =
    slf4j.Slf4jLogger.getLoggerFromName[IO](s"${getClass.getCanonicalName}.setup.report")

  /**
    * Should be overiden to create a connection config appropriate for the test
    */
  def dbConfig(meta: TestData): DBConnectionConfig

  //hack to remove unused param error
  def flywayConfig(meta: TestData): Option[FlywayConfig] = Option(meta) >> Option.empty

  def dbTransactor(meta: TestData)(implicit rt: PureharmTestRuntime): Resource[IO, DBTransactor]

  def _fixture(meta: TestData)(implicit rt: PureharmTestRuntime): Resource[IO, DBTransactor] =
    for {
      _       <- report.info(MDCKeys(meta))("SETUP — init").to[Resource[IO, *]]
      _       <- _cleanDB(meta)
      _       <- _initDB(meta)
      fixture <- dbTransactor(meta)
    } yield fixture

  def _initDB(meta: TestData)(implicit rt: PureharmTestRuntime): Resource[IO, Unit] =
    for {
      _ <- report.info(MDCKeys(meta))("SETUP — preparing DB").to[Resource[IO, *]]
      _ <- flyway.Flyway.migrate[IO](dbConfig = dbConfig(meta), flywayConfig(meta)).to[Resource[IO, *]]
      _ <- report.info(MDCKeys(meta))("SETUP — done preparing DB").to[Resource[IO, *]]
    } yield ()

  def _cleanDB(meta: TestData)(implicit rt: PureharmTestRuntime): Resource[IO, Unit] =
    for {
      _ <- report.info(MDCKeys(meta))("SETUP — cleaning DB for a clean slate").to[Resource[IO, *]]
      _ <- flyway.Flyway.clean[IO](dbConfig(meta)).to[Resource[IO, *]]
      _ <- report.info(MDCKeys(meta))("SETUP — done cleaning DB").to[Resource[IO, *]]
    } yield ()
}
