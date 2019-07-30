package busymachines.pureharm.db.flyway.internals

import busymachines.pureharm.db.flyway.FlywayConfig

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 30 Jul 2019
  *
  */
private[db] trait FlywayConfigFluentApi {
  def withLocations(locations: String*):      FlywayConfig
  def withLocations(locations: List[String]): FlywayConfig

  def withSchemas(schemas: String*):      FlywayConfig
  def withSchemas(schemas: List[String]): FlywayConfig

  def withIgnoreMissingMigrations(ignore: Boolean): FlywayConfig
  def withCleanOnValidationErrors(clean:  Boolean): FlywayConfig

  def defaultConfig: FlywayConfig
}
