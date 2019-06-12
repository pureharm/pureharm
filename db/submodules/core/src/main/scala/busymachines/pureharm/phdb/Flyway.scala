package busymachines.pureharm.phdb

import org.flywaydb.core.{Flyway => JFlyway}

import busymachines.pureharm.db._
import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 12 Jun 2019
  *
  */
object Flyway {

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
    migrationLocations: List[String] = List.empty,
  )(
    implicit
    F: Sync[F],
  ): F[Int] =
    for {
      fw      <- flywayInit[F](url, username, password, migrationLocations)
      applied <- Sync[F].delay(fw.migrate())
    } yield applied

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
