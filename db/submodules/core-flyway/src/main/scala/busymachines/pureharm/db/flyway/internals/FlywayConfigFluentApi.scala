package busymachines.pureharm.db.flyway.internals

import busymachines.pureharm.db.flyway._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 30 Jul 2019
  *
  */
private[db] trait FlywayConfigFluentApi {
  def withLocations(locations: MigrationLocation*):      FlywayConfig
  def withLocations(locations: List[MigrationLocation]): FlywayConfig

  def withSchemas(schemas: SchemaName*):      FlywayConfig
  def withSchemas(schemas: List[SchemaName]): FlywayConfig

  def withIgnoreMissingMigrations(ignore: Boolean): FlywayConfig
  def withCleanOnValidationErrors(clean:  Boolean): FlywayConfig

  def defaultConfig: FlywayConfig
}
