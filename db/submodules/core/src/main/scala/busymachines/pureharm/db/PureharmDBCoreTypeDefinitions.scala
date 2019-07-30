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

import busymachines.pureharm.db

/**
  *
  * This trait provides convenience aliases to mix in directly
  * into client code in order to minimize imports.
  *
  * E.g:
  * {{{
  *   package com.domainspecific
  *
  *   package object db extends busymachines.pureharm.dbslick.types.DBCoreTypes // +driver specific ones {
  *     type DomainSpecificType = Int
  *   }
  * }}}
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 02 Apr 2019
  *
  */
trait PureharmDBCoreTypeDefinitions {

  final val DBHost: db.DBHost.type = db.DBHost
  final type DBHost = db.DBHost

  final val JDBCUrl: db.JDBCUrl.type = db.JDBCUrl
  final type JDBCUrl = db.JDBCUrl.Type

  final val DBUsername: db.DBUsername.type = db.DBUsername
  final type DBUsername = db.DBUsername.Type

  final val DBPassword: db.DBPassword.type = db.DBPassword
  final type DBPassword = db.DBPassword.Type

  final val TableName = db.TableName
  final type TableName = db.TableName.Type

  final val DatabaseName = db.DatabaseName
  final type DatabaseName = db.DatabaseName.Type

  final type DAOAlgebra[R[_], E, PK] = db.DAOAlgebra[R, E, PK]

  final val Flyway: db.Flyway.type = db.Flyway
}
