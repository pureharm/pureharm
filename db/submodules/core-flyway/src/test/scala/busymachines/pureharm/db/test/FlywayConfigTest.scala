/**
  * Copyright (c) 2019 BusyMachines
  *
  * See company homepage at: https://www.busymachines.com/
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
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

  override def fixture(meta: MetaData): Resource[IO, Unit] = Resource.pure[IO, Unit](())

  override type FixtureParam = Unit

  private val `"public"`            = SchemaName("public")
  private val `"db/migration"`      = MigrationLocation("db/migration")
  private val `"db/test_migration"` = MigrationLocation("db/test_migration")

  test("read config with all fields from reference.conf") { _ =>
    FlywayConfig.fromNamespace[IO]("pureharm.db.migrations.migration1").map { config =>
      assert(
        config === FlywayConfig
          .withSchemas(`"public"`)
          .withLocations(`"db/migration"`, `"db/test_migration"`)
          .withCleanOnValidationErrors(CleanOnValidationError.True)
          .withIgnoreMissingMigrations(IgnoreMissingMigrations.True)
      )
    }
  }

  test("read config with missing schemas field — should use default") { _ =>
    FlywayConfig.fromNamespace[IO]("pureharm.db.migrations.migration2").map { config =>
      assert(
        config == FlywayConfig
          .withLocations(`"db/migration"`, `"db/test_migration"`)
          .withCleanOnValidationErrors(CleanOnValidationError.True)
          .withIgnoreMissingMigrations(IgnoreMissingMigrations.True)
      )
    }
  }

  test("read config with missing locations field — should use default") { _ =>
    FlywayConfig.fromNamespace[IO]("pureharm.db.migrations.migration3").map { config =>
      assert(
        config == FlywayConfig
          .withSchemas(`"public"`)
          .withCleanOnValidationErrors(CleanOnValidationError.True)
          .withIgnoreMissingMigrations(IgnoreMissingMigrations.True)
      )
    }
  }

  test("read config with missing ignoreMissingMigrations field — should use default") { _ =>
    FlywayConfig.fromNamespace[IO]("pureharm.db.migrations.migration4").map { config =>
      assert(
        config == FlywayConfig
          .withSchemas(`"public"`)
          .withLocations(`"db/migration"`, `"db/test_migration"`)
          .withCleanOnValidationErrors(CleanOnValidationError.True)
      )
    }
  }

  test("read config with missing cleanOnValidationError field — should use default") { _ =>
    FlywayConfig.fromNamespace[IO]("pureharm.db.migrations.migration5").map { config =>
      assert(
        config == FlywayConfig
          .withSchemas(`"public"`)
          .withLocations(`"db/migration"`, `"db/test_migration"`)
          .withIgnoreMissingMigrations(IgnoreMissingMigrations.True)
      )
    }
  }

  test("read config with all missing fields — should just use default") { _ =>
    FlywayConfig.fromNamespace[IO]("pureharm.db.migrations.migration6").map { config =>
      assert(
        config == FlywayConfig.defaultConfig
      )
    }
  }

  test("read config with single string schemas as List(schema)") { _ =>
    FlywayConfig.fromNamespace[IO]("pureharm.db.migrations.migration7").map { config =>
      assert(
        config == FlywayConfig
          .withSchemas(`"public"`)
      )
    }
  }

  test("read config with single string locations as List(location)") { _ =>
    FlywayConfig.fromNamespace[IO]("pureharm.db.migrations.migration8").map { config =>
      assert(
        config == FlywayConfig
          .withLocations(`"db/test_migration"`)
      )
    }
  }

}
