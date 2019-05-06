package busymachines.pureharm

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 06 May 2019
  *
  */
object db {
  final object JDBCUrl extends PhantomType[String]
  final type JDBCUrl = JDBCUrl.Type

  final object DBUsername extends PhantomType[String]
  final type DBUsername = DBUsername.Type

  final object DBPassword extends PhantomType[String]
  final type DBPassword = DBPassword.Type

  final object TableName extends PhantomType[String]
  final type TableName = TableName.Type

  final object ConnectionIOEC extends PhantomType[scala.concurrent.ExecutionContext]
  final type ConnectionIOEC = ConnectionIOEC.Type

  final type DAOAlgebra[R[_], E, PK] = db_impl.DAOAlgebra[R, E, PK]

  final type PureharmDBCoreTypeDefinitions = db_impl.PureharmDBCoreTypeDefinitions
}
