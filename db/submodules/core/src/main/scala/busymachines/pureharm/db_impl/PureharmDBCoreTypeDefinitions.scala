package busymachines.pureharm.db_impl

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

  final val JDBCUrl: db.JDBCUrl.type = db.JDBCUrl
  final type JDBCUrl = db.JDBCUrl

  final val DBUsername: db.DBUsername.type = db.DBUsername
  final type DBUsername = db.DBUsername

  final val DBPassword: db.DBPassword.type = db.DBPassword
  final type DBPassword = db.DBPassword

  final val TableName = db.TableName
  final type TableName = db.TableName

  final type DAOAlgebra[R[_], E, PK] = _root_.busymachines.pureharm.db_impl.DAOAlgebra[R, E, PK]

  type ConnectionIOEC = db.ConnectionIOEC
  val ConnectionIOEC: db.ConnectionIOEC.type = db.ConnectionIOEC
}

