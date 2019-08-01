package busymachines.pureharm.db

import busymachines.pureharm.phantom._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 30 Jul 2019
  *
  */
package object flyway {

  object MigrationLocation extends PhantomType[String]
  type MigrationLocation = MigrationLocation.Type

  object SchemaName extends PhantomType[String]
  type SchemaName = SchemaName.Type

  object IgnoreMissingMigrations extends PhantomType[Boolean] {
    val False: this.Type = this.apply(false)
    val True:  this.Type = this.apply(true)
  }
  type IgnoreMissingMigrations = IgnoreMissingMigrations.Type

  object CleanOnValidationError extends PhantomType[Boolean] {
    val False: this.Type = this.apply(false)
    val True:  this.Type = this.apply(true)
  }
  type CleanOnValidationError = CleanOnValidationError.Type
}
