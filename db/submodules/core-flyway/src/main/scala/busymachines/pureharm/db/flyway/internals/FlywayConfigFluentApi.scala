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
package busymachines.pureharm.db.flyway.internals

import busymachines.pureharm.db.flyway._

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 30 Jul 2019
  */
private[db] trait FlywayConfigFluentApi {
  def withLocations(locations: MigrationLocation*):      FlywayConfig
  def withLocations(locations: List[MigrationLocation]): FlywayConfig

  def withSchemas(schemas: SchemaName*):      FlywayConfig
  def withSchemas(schemas: List[SchemaName]): FlywayConfig

  def withIgnoreMissingMigrations(ignore: IgnoreMissingMigrations): FlywayConfig
  def withCleanOnValidationErrors(clean:  CleanOnValidationError):  FlywayConfig

  def defaultConfig: FlywayConfig
}
