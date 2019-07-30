package busymachines.pureharm.db.test

import busymachines.pureharm.db.flyway._
import busymachines.pureharm.effects._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 30 Jul 2019
  *
  */
final class FlywayConfigTest extends PureharmFixtureTest {

  override def fixture: Resource[IO, Unit] = Resource.pure(())

  override type FixtureParam = Unit

  private val `"public"`            = SchemaName("public")
  private val `"db/migration"`      = MigrationLocation("db/migration")
  private val `"db/test_migration"` = MigrationLocation("db/test_migration")

  iotest("read config with all fields from reference.conf") { _ =>
    FlywayConfig.fromNamespace[IO]("pureharm.db.migrations.migration1").map { config =>
      assert(
        config === FlywayConfig
          .withSchemas(`"public"`)
          .withLocations(`"db/migration"`, `"db/test_migration"`)
          .withCleanOnValidationErrors(true)
          .withIgnoreMissingMigrations(true),
      )
    }
  }

  iotest("read config with missing schemas field — should use default") { _ =>
    FlywayConfig.fromNamespace[IO]("pureharm.db.migrations.migration2").map { config =>
      assert(
        config == FlywayConfig
          .withLocations(`"db/migration"`, `"db/test_migration"`)
          .withCleanOnValidationErrors(true)
          .withIgnoreMissingMigrations(true),
      )
    }
  }

  iotest("read config with missing locations field — should use default") { _ =>
    FlywayConfig.fromNamespace[IO]("pureharm.db.migrations.migration3").map { config =>
      assert(
        config == FlywayConfig
          .withSchemas(`"public"`)
          .withCleanOnValidationErrors(true)
          .withIgnoreMissingMigrations(true),
      )
    }
  }

  iotest("read config with missing ignoreMissingMigrations field — should use default") { _ =>
    FlywayConfig.fromNamespace[IO]("pureharm.db.migrations.migration4").map { config =>
      assert(
        config == FlywayConfig
          .withSchemas(`"public"`)
          .withLocations(`"db/migration"`, `"db/test_migration"`)
          .withCleanOnValidationErrors(true),
      )
    }
  }

  iotest("read config with missing cleanOnValidationError field — should use default") { _ =>
    FlywayConfig.fromNamespace[IO]("pureharm.db.migrations.migration5").map { config =>
      assert(
        config == FlywayConfig
          .withSchemas(`"public"`)
          .withLocations(`"db/migration"`, `"db/test_migration"`)
          .withIgnoreMissingMigrations(true),
      )
    }
  }

  iotest("read config with all missing fields — should just use default") { _ =>
    FlywayConfig.fromNamespace[IO]("pureharm.db.migrations.migration6").map { config =>
      assert(
        config == FlywayConfig.defaultConfig,
      )
    }
  }

  iotest("read config with single string schemas as List(schema)") { _ =>
    FlywayConfig.fromNamespace[IO]("pureharm.db.migrations.migration7").map { config =>
      assert(
        config == FlywayConfig
          .withSchemas(`"public"`),
      )
    }
  }

  iotest("read config with single string locations as List(location)") { _ =>
    FlywayConfig.fromNamespace[IO]("pureharm.db.migrations.migration8").map { config =>
      assert(
        config == FlywayConfig
          .withLocations(`"db/test_migration"`),
      )
    }
  }

}
