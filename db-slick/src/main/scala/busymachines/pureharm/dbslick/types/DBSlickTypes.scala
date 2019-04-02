package busymachines.pureharm.dbslick.types

import busymachines.pureharm.core.PhantomType

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 02 Apr 2019
  *
  */
trait DBSlickTypes {
  type ConnectionIO[T] = slick.dbio.DBIO[T]
  val ConnectionIO: slick.dbio.DBIO.type = slick.dbio.DBIO

  type SlickDB = slick.jdbc.PostgresProfile#API#Database

  object JDBCUrl extends PhantomType[String]
  type JDBCUrl = JDBCUrl.Phantom

  object DBUsername extends PhantomType[String]
  type DBUsername = DBUsername.Phantom

  object DBPassword extends PhantomType[String]
  type DBPassword = DBPassword.Phantom
}
