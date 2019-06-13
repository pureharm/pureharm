package busymachines.pureharm

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 13 Jun 2019
  *
  */
package object db {
  final object JDBCUrl extends PhantomType[String] {

    def postgresql(port: Int, host: String, db: DatabaseName): this.Type =
      this.apply(s"jdbc:postgresql://$host:$port/$db")
  }

  final type JDBCUrl = JDBCUrl.Type

  final object DBUsername extends PhantomType[String]
  final type DBUsername = DBUsername.Type

  final object DBPassword extends PhantomType[String]
  final type DBPassword = DBPassword.Type

  final object TableName extends PhantomType[String]
  final type TableName = TableName.Type

  final object DatabaseName extends PhantomType[String]
  final type DatabaseName = DatabaseName.Type
}
