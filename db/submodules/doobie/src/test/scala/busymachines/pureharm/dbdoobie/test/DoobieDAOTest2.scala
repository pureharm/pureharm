package busymachines.pureharm.dbdoobie.test

import busymachines.pureharm.db._
import busymachines.pureharm.db.test._
import busymachines.pureharm.db.testkit._
import busymachines.pureharm.dbdoobie._
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
//TODO: move to doobie-testkit
final class DoobieDAOTest2 extends PureharmRowTest[Transactor[IO]] {
  import runtime._
  override type FixtureParam = DoobiePureharmRowDAO[IO]

  override def setup: PureharmDAOTestSetup[Transactor[IO]] = DoobieDAOTest2

  override def fixture(meta: MetaData, trans: Transactor[IO]): Resource[IO, DoobiePureharmRowDAO[IO]] =
    Resource.pure[IO, DoobiePureharmRowDAO[IO]](DoobiePureharmRowDAO(trans))

}

object DoobieDAOTest2 extends PureharmDAOTestSetup[Transactor[IO]] {

  /**
    * Should be overiden to create a connection config appropriate for the test
    */
  override def dbConfig(meta: TestData): DBConnectionConfig =
    PureharmTestConfig.dbConfig.copy(
      schema = PureharmTestConfig.schemaName(s"doobie")
    )

  override def dbTransactor(meta: TestData)(implicit rt: PureharmTestRuntime): Resource[IO, Transactor[IO]] = {
    val config = dbConfig(meta)
    import rt.contextShift
    Transactor
      .fromDriverManager[IO](
        driver  = "org.postgresql.Driver", // driver classname
        url     = config.jdbcURL: String,
        user    = config.username: String,
        pass    = config.password: String,
        blocker = rt.blocker,
      )
      .pure[Resource[IO, *]]
      .widen[Transactor[IO]]
  }
}
