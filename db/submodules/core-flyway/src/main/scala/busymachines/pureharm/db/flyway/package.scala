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
package busymachines.pureharm.db

import busymachines.pureharm.phantom._

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 30 Jul 2019
  */
package object flyway {

  object MigrationLocation extends PhantomType[String] {
    /**
      * The default location of flyway migrations
      */
    def default: this.Type = this.apply("db/migration")
  }
  type MigrationLocation = MigrationLocation.Type

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
