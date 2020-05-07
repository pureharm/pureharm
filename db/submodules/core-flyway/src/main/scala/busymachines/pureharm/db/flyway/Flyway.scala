/**
  * Copyright (c) 2017-2019 BusyMachines
  *
  * See company homepage at: https://www.busymachines.com/
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package busymachines.pureharm.db.flyway

import busymachines.pureharm.db._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 30 Jul 2019
  *
  */
object Flyway {
  import org.flywaydb.core.{Flyway => JFlyway}

  import busymachines.pureharm.effects._
  import busymachines.pureharm.effects.implicits._

  def migrate[F[_]](
    url:          JDBCUrl,
    username:     DBUsername,
    password:     DBPassword,
    flywayConfig: Option[FlywayConfig],
  )(implicit
    F:            Sync[F]
  ): F[Int] =
    for {
      fw   <- flywayInit[F](url, username, password, flywayConfig)
      migs <- F.delay(fw.migrate())
    } yield migs

  def migrate[F[_]](
    dbConfig:     DBConnectionConfig,
    flywayConfig: Option[FlywayConfig] = Option.empty,
  )(implicit
    F:            Sync[F]
  ): F[Int] =
    for {
      fw   <- flywayInit[F](dbConfig.jdbcURL, dbConfig.username, dbConfig.password, flywayConfig)
      migs <- F.delay(fw.migrate())
    } yield migs

  def clean[F[_]: Sync](dbConfig: DBConnectionConfig): F[Unit] =
    this.clean[F](url = dbConfig.jdbcURL, username = dbConfig.username, password = dbConfig.password)

  def clean[F[_]: Sync](url:      JDBCUrl, username: DBUsername, password: DBPassword): F[Unit] =
    for {
      fw <- flywayInit[F](url, username, password, Option.empty)
      _  <- Sync[F].delay(fw.clean())
    } yield ()

  private def flywayInit[F[_]](
    url:        JDBCUrl,
    username:   DBUsername,
    password:   DBPassword,
    config:     Option[FlywayConfig],
  )(implicit F: Sync[F]): F[JFlyway] =
    F.delay {
      val fwConfig = JFlyway.configure()
      fwConfig.dataSource(url, username, password)
      fwConfig.mixed(true)
      config match {
        case None    => () //default everything. Do nothing, lol, java
        case Some(c) =>
          if (c.migrationLocations.nonEmpty) {
            fwConfig.locations(c.migrationLocations: _*)
          }
          if (c.schemas.nonEmpty) {
            fwConfig.schemas(c.schemas: _*)
          }
          fwConfig.ignoreMissingMigrations(c.ignoreMissingMigrations)
          fwConfig.cleanOnValidationError(c.cleanOnValidationError)
      }

      new JFlyway(fwConfig)
    }
}
