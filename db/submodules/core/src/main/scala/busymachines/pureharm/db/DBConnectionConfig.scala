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

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 16 Jun 2019
  *
  */
final case class DBConnectionConfig(
  host:     DBHost,
  dbName:   DatabaseName,
  username: DBUsername,
  password: DBPassword,
) {

  def jdbcURL: JDBCUrl = JDBCUrl.postgresql(host, dbName)
}

import busymachines.pureharm.config._

object DBConnectionConfig extends ConfigLoader[DBConnectionConfig] {
  import busymachines.pureharm.effects.Sync
  import busymachines.pureharm.config.implicits._

  implicit override def configReader: ConfigReader[DBConnectionConfig] = semiauto.deriveReader[DBConnectionConfig]

  override def default[F[_]: Sync]: F[DBConnectionConfig] =
    this.load("pureharm.db.connection")
}
