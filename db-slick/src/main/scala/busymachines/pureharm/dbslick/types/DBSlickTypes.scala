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

  final type SlickDB  = dbslick.SlickDB
  final type SlickAPI = dbslick.SlickAPI

  final val JDBCUrl: dbslick.JDBCUrl.type = dbslick.JDBCUrl
  final type JDBCUrl = dbslick.JDBCUrl.Type

  final val DBUsername: dbslick.DBUsername.type = dbslick.DBUsername
  final type DBUsername = dbslick.DBUsername.Type

  final val DBPassword: dbslick.DBPassword.type = dbslick.DBPassword
  final type DBPassword = dbslick.DBPassword.Type

  final type JdbcProfileAPI = dbslick.JdbcProfileAPI
  final val JdbcProfileAPI: dbslick.JdbcProfileAPI.type = dbslick.JdbcProfileAPI

  final type Transactor[F[_]] = dbslick.Transactor[F]
  final val Transactor: dbslick.Transactor.type = dbslick.Transactor

  final type DBBlockingIOExecutionConfig = dbslick.DBBlockingIOExecutionConfig
  final val DBBlockingIOExecutionConfig: dbslick.DBBlockingIOExecutionConfig.type = dbslick.DBBlockingIOExecutionConfig
}
