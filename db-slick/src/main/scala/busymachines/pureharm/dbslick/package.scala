package busymachines.pureharm

import busymachines.pureharm.core.PhantomType
import busymachines.pureharm.dbslick.types.ConnectionIOCatsInstances

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 02 Apr 2019
  *
  */
package object dbslick extends ConnectionIOCatsInstances {
  final type ConnectionIO[T] = slick.dbio.DBIO[T]
  final val ConnectionIO: slick.dbio.DBIO.type = slick.dbio.DBIO

  final type SlickBackendDB      = slick.jdbc.JdbcProfile#Backend#Database
  final type SlickJDBCProfileAPI = slick.jdbc.JdbcProfile#API

  final object JDBCUrl extends PhantomType[String]
  final type JDBCUrl = JDBCUrl.Type

  final object DBUsername extends PhantomType[String]
  final type DBUsername = DBUsername.Type

  final object DBPassword extends PhantomType[String]
  final type DBPassword = DBPassword.Type

  final object JDBCProfileAPI extends PhantomType[SlickJDBCProfileAPI]
  final type JDBCProfileAPI = JDBCProfileAPI.Type

  final object DatabaseBackend extends PhantomType[SlickBackendDB]
  final type DatabaseBackend = DatabaseBackend.Type

  final object TableName extends PhantomType[String]
  final type TableName = TableName.Type
}
