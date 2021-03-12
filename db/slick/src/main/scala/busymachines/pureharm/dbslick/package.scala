/** Copyright (c) 2019 BusyMachines
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
package busymachines.pureharm

import busymachines.pureharm.phantom._

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 13 Jun 2019
  */
package object dbslick {
  final type ConnectionIO[T] = slick.dbio.DBIO[T]
  final val ConnectionIO: slick.dbio.DBIO.type = slick.dbio.DBIO

  final type SlickBackendDB      = slick.jdbc.JdbcProfile#Backend#Database
  final type SlickSession        = slick.jdbc.JdbcProfile#Backend#Session
  final type SlickJDBCProfileAPI = slick.jdbc.JdbcProfile#API

  final object JDBCProfileAPI extends SproutSub[SlickJDBCProfileAPI]
  final type JDBCProfileAPI = JDBCProfileAPI.Type

  final object DatabaseBackend extends SproutSub[SlickBackendDB]
  final type DatabaseBackend = DatabaseBackend.Type

  final object DatabaseSession extends SproutSub[SlickSession]
  final type DatabaseSession = DatabaseSession.Type

  final object ConnectionIOEC extends SproutSub[scala.concurrent.ExecutionContext]
  final type ConnectionIOEC = ConnectionIOEC.Type
}
