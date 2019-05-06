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
package busymachines.pureharm.dbslick.types

import busymachines.pureharm.dbslick

/**
  *
  * This trait provides convenience aliases to mix in directly
  * into client code in order to minimize imports.
  *
  * E.g:
  * {{{
  *   package com.domainspecific
  *
  *   package object db extends busymachines.pureharm.dbslick.types.DBSlickTypes {
  *     type DomainSpecificType = Int
  *   }
  * }}}
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 02 Apr 2019
  *
  */
trait PureharmDBSlickTypeDefinitions {

  final type ConnectionIO[T] = dbslick.ConnectionIO[T]
  final val ConnectionIO: slick.dbio.DBIO.type = slick.dbio.DBIO

  final type SlickDB  = dbslick.SlickBackendDB
  final type SlickAPI = dbslick.SlickJDBCProfileAPI

  final val JDBCProfileAPI: dbslick.JDBCProfileAPI.type = dbslick.JDBCProfileAPI
  final type JDBCProfileAPI = dbslick.JDBCProfileAPI

  final val DatabaseBackend: dbslick.DatabaseBackend.type = dbslick.DatabaseBackend
  final type DatabaseBackend = dbslick.DatabaseBackend

  final type Transactor[F[_]] = dbslick.Transactor[F]
  final val Transactor: dbslick.Transactor.type = dbslick.Transactor

  final type DBBlockingIOExecutionConfig = dbslick.SlickDBIOAsyncExecutorConfig
  final val DBBlockingIOExecutionConfig: dbslick.SlickDBIOAsyncExecutorConfig.type =
    dbslick.SlickDBIOAsyncExecutorConfig

}
