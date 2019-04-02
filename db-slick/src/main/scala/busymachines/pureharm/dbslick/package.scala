package busymachines.pureharm

import busymachines.pureharm.core.PhantomType

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 02 Apr 2019
  *
  */
package object dbslick {
  final type ConnectionIO[T] = slick.dbio.DBIO[T]
  final val ConnectionIO: slick.dbio.DBIO.type = slick.dbio.DBIO

  final type SlickDB  = slick.jdbc.JdbcProfile#Backend#Database
  final type SlickAPI = slick.jdbc.JdbcProfile#API

  final object JDBCUrl extends PhantomType[String]
  final type JDBCUrl = JDBCUrl.Type

  final object DBUsername extends PhantomType[String]
  final type DBUsername = DBUsername.Type

  final object DBPassword extends PhantomType[String]
  final type DBPassword = DBPassword.Type
}
