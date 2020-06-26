package busymachines.pureharm.dbdoobie.testkit

import busymachines.pureharm.db._
import busymachines.pureharm.db.testkit._
import busymachines.pureharm.dbdoobie._
import busymachines.pureharm.effects._
import busymachines.pureharm.testkit._
import org.scalatest._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 26 Jun 2020
  *
  */
trait PHTestSetupDoobie extends PureharmDAOTestSetup[Transactor[IO]] {

  /**
    * Should be overiden to create a connection config appropriate for the test
    */
  override def dbConfig(meta: TestData)(implicit logger: TestLogger): DBConnectionConfig

  override def dbTransactorInstance(
    meta:        TestData
  )(implicit rt: PureharmTestRuntime, logger: TestLogger): Resource[IO, Transactor[IO]] = {
    val config = dbConfig(meta)
    import rt.contextShift
    val r      = for {
      _     <- logger.info(MDCKeys(meta))(s"CREATING Transactor[IO] for: ${config.jdbcURL}")
      trans <- IO.pure[Transactor[IO]] {
        Transactor
          .fromDriverManager[IO](
            driver  = PHTestSetupDoobie.PostgresqlDriver,
            url     = config.jdbcURL: String,
            user    = config.username: String,
            pass    = config.password: String,
            blocker = rt.blocker,
          )
      }

    } yield trans
    r.to[Resource[IO, *]]
  }
}

object PHTestSetupDoobie {
  private val PostgresqlDriver: String = "org.postgresql.Driver"
}
