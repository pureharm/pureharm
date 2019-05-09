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
package busymachines.pureharm.phdbslick.definitions

import busymachines.pureharm.phdbslick
import busymachines.pureharm.phdbslick.slickTypes

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

  final type ConnectionIO[T] = slickTypes.ConnectionIO[T]
  final val ConnectionIO: slick.dbio.DBIO.type = slick.dbio.DBIO

  final type SlickDB  = slickTypes.SlickBackendDB
  final type SlickAPI = slickTypes.SlickJDBCProfileAPI

  final val JDBCProfileAPI: slickTypes.JDBCProfileAPI.type = slickTypes.JDBCProfileAPI
  final type JDBCProfileAPI = slickTypes.JDBCProfileAPI

  final val DatabaseBackend: slickTypes.DatabaseBackend.type = slickTypes.DatabaseBackend
  final type DatabaseBackend = slickTypes.DatabaseBackend

  final type Transactor[F[_]] = phdbslick.Transactor[F]
  final val Transactor: phdbslick.Transactor.type = phdbslick.Transactor

  final type SlickDBIOAsyncExecutorConfig = phdbslick.SlickDBIOAsyncExecutorConfig
  final val SlickDBIOAsyncExecutorConfig: phdbslick.SlickDBIOAsyncExecutorConfig.type =
    phdbslick.SlickDBIOAsyncExecutorConfig

}
