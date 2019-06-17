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
package busymachines.pureharm.db

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 12 Jun 2019
  *
  */
object Flyway {
  import org.flywaydb.core.{Flyway => JFlyway}

  import busymachines.pureharm.effects._
  import busymachines.pureharm.effects.implicits._

  /**
    * See overload
    *
    * @param migrationLocations
    *   give the locations for which Flyway searches for
    *   migration files. Defaults to:
    *   ``resource/db/migration`` folder on the classpath
    * @return
    *   the number of migrations
    */
  def migrate[F[_]](
    dbConfig:           DBConnectionConfig,
    migrationLocations: List[String],
  )(
    implicit
    F: Sync[F],
  ): F[Int] = this.migrate[F](
    url                = dbConfig.jdbcURL,
    username           = dbConfig.username,
    password           = dbConfig.password,
    migrationLocations = migrationLocations,
  )

  /**
    *
    * @param url
    *   the URL on which the DB server runs
    * @param migrationLocations
    *   give the locations for which Flyway searches for
    *   migration files. Defaults to:
    *   ``resource/db/migration`` folder on the classpath
    * @return
    *   the number of migrations
    */
  def migrate[F[_]](
    url:                JDBCUrl,
    username:           DBUsername,
    password:           DBPassword,
    migrationLocations: List[String],
  )(
    implicit
    F: Sync[F],
  ): F[Int] =
    for {
      fw      <- flywayInit[F](url, username, password, migrationLocations)
      applied <- Sync[F].delay(fw.migrate())
    } yield applied

  def clean[F[_]: Sync](dbConfig: DBConnectionConfig): F[Unit] =
    this.clean[F](url = dbConfig.jdbcURL, username = dbConfig.username, password = dbConfig.password)

  def clean[F[_]: Sync](url: JDBCUrl, username: DBUsername, password: DBPassword): F[Unit] = {
    for {
      fw <- flywayInit[F](url, username, password)
      _  <- Sync[F].delay(fw.clean())
    } yield ()
  }

  private def flywayInit[F[_]](
    url:                JDBCUrl,
    username:           DBUsername,
    password:           DBPassword,
    migrationLocations: List[String] = List.empty,
  )(implicit F:         Sync[F]): F[JFlyway] =
    F.delay {
      val fwConfig = JFlyway.configure()
      fwConfig.dataSource(url, username, password)
      fwConfig.mixed(true)
      if (migrationLocations.isEmpty) {
        //default location. Do nothing, lol, java
      }
      else {
        fwConfig.locations(migrationLocations: _*)
      }

      new JFlyway(fwConfig)
    }
}
