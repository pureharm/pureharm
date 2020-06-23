package busymachines.pureharm.dbslick.psql.test

import busymachines.pureharm.db._
import busymachines.pureharm.db.test.PureharmFixtureTest
import busymachines.pureharm.dbslick.{SlickDBIOAsyncExecutorConfig, Transactor}
import busymachines.pureharm.effects._

/**
  * @author Daniel Incicau, daniel.incicau@busymachines.com
  * @since 27/01/2020
  */
final class TransactorTest extends PureharmFixtureTest {

  private lazy val slickConfig: SlickDBIOAsyncExecutorConfig = SlickDBIOAsyncExecutorConfig.default

  /**
    * Instead of the "before and after shit" simply init, and close
    * everything in this Resource...
    */
  override def fixture: Resource[IO, Transactor[IO]] =
    for {
      dbConfig   <- Resource.pure[IO, DBConnectionConfig](PureharmTestConfig.dbConfig)
      transactor <-
        Transactor
          .pgSQLHikari[IO](dbProfile = testdb.jdbcProfileAPI, dbConnection = dbConfig, asyncConfig = slickConfig)
    } yield transactor

  override type FixtureParam = Transactor[IO]

  test("creates the transactor and the session connection is open from the start") { implicit trans: Transactor[IO] =>
    for {
      isConnected <- trans.isConnected
    } yield assert(isConnected === true)
  }

  test("closes the active connection") { implicit trans: Transactor[IO] =>
    for {
      isConnected <- trans.isConnected
      _ = assert(isConnected === true)
      _           <- trans.closeSession
      isConnected <- trans.isConnected
    } yield assert(isConnected === false)
  }

  test("recreates the connection when the current connection is open") { implicit trans: Transactor[IO] =>
    for {
      isConnected <- trans.isConnected
      _ = assert(isConnected === true)
      _           <- trans.recreateSession
      isConnected <- trans.isConnected
    } yield assert(isConnected === true)
  }

  test("recreates the connection when the current connection is closed") { implicit trans: Transactor[IO] =>
    for {
      isConnected <- trans.isConnected
      _ = assert(isConnected === true)
      _           <- trans.closeSession
      isConnected <- trans.isConnected
      _ = assert(isConnected === false)
      _           <- trans.recreateSession
      isConnected <- trans.isConnected
    } yield assert(isConnected === true)
  }

}
