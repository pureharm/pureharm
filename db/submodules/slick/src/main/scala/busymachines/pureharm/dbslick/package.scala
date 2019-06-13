package busymachines.pureharm

import busymachines.pureharm.phantom.PhantomType

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 13 Jun 2019
  *
  */
package object dbslick {
  final type ConnectionIO[T] = slick.dbio.DBIO[T]
  final val ConnectionIO: slick.dbio.DBIO.type = slick.dbio.DBIO

  final type SlickBackendDB      = slick.jdbc.JdbcProfile#Backend#Database
  final type SlickJDBCProfileAPI = slick.jdbc.JdbcProfile#API

  final object JDBCProfileAPI extends PhantomType[SlickJDBCProfileAPI]
  final type JDBCProfileAPI = JDBCProfileAPI.Type

  final object DatabaseBackend extends PhantomType[SlickBackendDB]
  final type DatabaseBackend = DatabaseBackend.Type

  final object ConnectionIOEC extends PhantomType[scala.concurrent.ExecutionContext]
  final type ConnectionIOEC = ConnectionIOEC.Type
}
