package busymachines.pureharm.db.test

import busymachines.pureharm.db._
import busymachines.pureharm.effects._
/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 17 Jun 2019
  *
  */
final class DBConnectionConfigTest extends PureharmFixtureTest {

  override def fixture: Resource[IO, Unit] = Resource.pure(())

  override type FixtureParam = Unit

  iotest("read config from default reference.conf") { _ =>
    DBConnectionConfig.default[IO].map { config =>
      assert(
        config == DBConnectionConfig(
          host     = DBHost("localhost:20010"),
          dbName   = DatabaseName("pureharm_test"),
          username = DBUsername("pureharmony"),
          password = DBPassword("pureharmony"),
        ),
      )
    }
  }
}
