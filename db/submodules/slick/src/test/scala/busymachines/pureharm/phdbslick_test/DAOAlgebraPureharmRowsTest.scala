package busymachines.pureharm.phdbslick_test

import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._
import busymachines.pureharm.phdb.Flyway
import db._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 12 Jun 2019
  *
  */

final class DAOAlgebraPureharmRowsTest extends PureharmFixtureTest {
  override type FixtureParam = PureharmRowDAO[IO]
  override def fixture: Resource[IO, PureharmRowDAO[IO]] =
    DAOAlgebraPureharmRowsTest.transactorResource.map(implicit t => PureharmRowDAO[IO](t, connectionIOEC))

  iotest("write single row + read by PK") { implicit dao: PureharmRowDAO[IO] =>
    val row = PureharmRow(
      id         = PhantomPK("test1"),
      byte       = PhantomByte(245.toByte),
      int        = PhantomInt(41),
      long       = PhantomLong(0.toLong),
      bigDecimal = PhantomBigDecimal(BigDecimal("1390749832749238")),
      string     = PhantomString("first_test_in_a_while"),
    )

    for {
      _          <- dao.insert(row)
      fetchedRow <- dao.find(row.id).flattenOption(fail(s"PK=${row.id} row was not in database"))
    } yield assert(row === fetchedRow)
  }
}

private[phdbslick_test] object DAOAlgebraPureharmRowsTest {

  /**
    * All these values come from this file:
    * db/docker-pureharm-postgresql-test.sh
    *
    */
  private val LocalPort    = 20010
  private val LocalHost    = "localhost"
  private val TestDBName   = DatabaseName("pureharm_test")
  private val PureharmUser = DBUsername("pureharmony")
  private val PureharmPwd  = DBPassword("pureharmony")
  private val url          = JDBCUrl.postgresql(LocalPort, LocalHost, TestDBName)

  def transactorResource(implicit cs: ContextShift[IO]): Resource[IO, Transactor[IO]] = {
    val trans = Transactor.pgSQLHikari[IO](db.jdbcProfileAPI)(
      url      = url,
      username = PureharmUser,
      password = PureharmPwd,
      config   = SlickDBIOAsyncExecutorConfig.default,
    )

    //no cleanup afterwards because the transactor is shutdown before a next flatMap
    cleanDB >> initDB >> trans
  }

  private def initDB: Resource[IO, Unit] = Resource.liftF[IO, Unit] {
    for {
      _ <- Flyway.migrate[IO](
        url                = url,
        username           = PureharmUser,
        password           = PureharmPwd,
        migrationLocations = List.empty,
      )
    } yield ()
  }

  private def cleanDB: Resource[IO, Unit] = Resource.liftF[IO, Unit] {
    for {
      _ <- Flyway.clean[IO](
        url      = url,
        username = PureharmUser,
        password = PureharmPwd,
      )
    } yield ()
  }
}
