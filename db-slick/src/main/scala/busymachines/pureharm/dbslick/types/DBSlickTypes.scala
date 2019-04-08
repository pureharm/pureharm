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
trait DBSlickTypes {
  final type ConnectionIO[T] = dbslick.ConnectionIO[T]
  final val ConnectionIO: slick.dbio.DBIO.type = slick.dbio.DBIO

  final type SlickDB  = dbslick.SlickBackendDB
  final type SlickAPI = dbslick.SlickJDBCProfileAPI

  final val JDBCUrl: dbslick.JDBCUrl.type = dbslick.JDBCUrl
  final type JDBCUrl = dbslick.JDBCUrl

  final val DBUsername: dbslick.DBUsername.type = dbslick.DBUsername
  final type DBUsername = dbslick.DBUsername

  final val DBPassword: dbslick.DBPassword.type = dbslick.DBPassword
  final type DBPassword = dbslick.DBPassword

  final val TableName = dbslick.TableName
  final type TableName = dbslick.TableName

  final val JDBCProfileAPI: dbslick.JDBCProfileAPI.type = dbslick.JDBCProfileAPI
  final type JDBCProfileAPI = dbslick.JDBCProfileAPI

  final val DatabaseBackend: dbslick.DatabaseBackend.type = dbslick.DatabaseBackend
  final type DatabaseBackend = dbslick.DatabaseBackend

  final type Transactor[F[_]] = dbslick.Transactor[F]
  final val Transactor: dbslick.Transactor.type = dbslick.Transactor

  final type DBBlockingIOExecutionConfig = dbslick.DBBlockingIOExecutionConfig
  final val DBBlockingIOExecutionConfig: dbslick.DBBlockingIOExecutionConfig.type = dbslick.DBBlockingIOExecutionConfig

  final type DAOAlgebra[R[_], E, PK] = dbslick.DAOAlgebra[R, E, PK]
}
