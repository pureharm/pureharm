package busymachines.pureharm.db

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 16 Jun 2019
  *
  */
final case class DBConnectionConfig(
  host:     DBHost,
  port:     DBPort,
  dbName:   DatabaseName,
  username: DBUsername,
  password: DBPassword,
) {
  def jdbcURL: JDBCUrl = JDBCUrl.postgresql(host, port, dbName)
}

import busymachines.pureharm.config._

object DBConnectionConfig extends ConfigLoader[DBConnectionConfig] {
  import busymachines.pureharm.effects.Sync
  import busymachines.pureharm.config.implicits._

  implicit override def configReader: ConfigReader[DBConnectionConfig] = semiauto.deriveReader[DBConnectionConfig]

  override def default[F[_]: Sync]: F[DBConnectionConfig] =
    this.load("pureharm.db.connection")
}
