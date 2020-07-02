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
package busymachines.pureharm.internals.dbdoobie

import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._
import busymachines.pureharm.db._
import busymachines.pureharm.dbdoobie._
import busymachines.pureharm.dbdoobie.implicits._

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 24 Sep 2019
  */
abstract class DoobieRepoQueries[E, PK, Table <: TableWithPK[E, PK]] extends Repo[ConnectionIO, E, PK] {
  def table: Table

  implicit protected def getPKImplicit:  Get[PK]  = table.metaPK.get
  implicit protected def putPKImplicit:  Put[PK]  = table.metaPK.put
  implicit protected def readEImplicit:  Read[E]  = table.readE
  implicit protected def writeEImplicit: Write[E] = table.writeE
  implicit protected def showPKImplicit: Show[PK] = table.showPK

  object frags {

    def array[T: Put](fs: List[T]): Fragment =
      fs.map(n => fr"$n").intercalate(fr",")

    def inArray[T: Put](fs: List[T]): Fragment =
      fr"IN " ++ inParens(array(fs))

    def inParens(f: Fragment): Fragment = op ++ f ++ cp

    private val op: Fragment = Fragment.const("(")
    private val cp: Fragment = Fragment.const(")")
  }

  override def find(pk: PK): ConnectionIO[Option[E]] =
    Query[PK, E](findSQL).option(pk)

  override def retrieve(pk: PK)(implicit show: Show[PK]): ConnectionIO[E] =
    this.find(pk).flattenOption(DBEntryNotFoundAnomaly(show.show(pk), Option.empty))

  override def insert(e: E): ConnectionIO[PK] =
    Update[E](insertSQL).withUniqueGeneratedKeys[PK](table.row.pkColumn)(e).adaptError(PSQLExceptionInterpreters.adapt)

  override def insertMany(es: Iterable[E]): ConnectionIO[Unit] = {
    val expectedSize = es.size
    for {
      inserted <- Update[E](insertSQL).updateMany(es).adaptError {
        case bux: java.sql.BatchUpdateException =>
          DBBatchInsertFailedAnomaly(
            expectedSize = expectedSize,
            actualSize   = 0,
            causedBy     = Option(bux),
          )
      }
      _        <- (inserted != expectedSize).ifTrueRaise[ConnectionIO](
        DBBatchInsertFailedAnomaly(
          expectedSize = expectedSize,
          actualSize   = inserted,
          causedBy     = Option.empty,
        )
      )
    } yield ()
  }

  override def update(e: E): ConnectionIO[E] =
    Update[(E, PK)](updateSQL).withUniqueGeneratedKeys[E](table.row.columnNames: _*)((e, table.row.pkValueOf(e)))

  //FIXME: rewrite with postgresql batch update
  override def updateMany[M[_]: Traverse](es: M[E]): ConnectionIO[Unit] =
    es.traverse(this.update).void

  override def delete(pk: PK): ConnectionIO[Unit] =
    for {
      deleted <- Update[PK](deleteSQL).run(pk)
      _       <- (deleted == 1).ifFalseRaise[ConnectionIO](DBDeleteByPKFailedAnomaly(pk.show))
    } yield ()

  override def deleteMany(pks: Iterable[PK]): ConnectionIO[Unit] = {
    val q = deleteManySQLFragment(pks.toList).update
    for {
      deleted <- q.run
      _       <- (deleted == pks.size).ifFalseRaise[ConnectionIO](DBDeleteByPKFailedAnomaly(pks.mkString(", ")))
    } yield ()
  }

  override def exists(pk: PK): ConnectionIO[Boolean] =
    Query[PK, Boolean](existsSQL).unique(pk)

  override def existsAtLeastOne(pks: Iterable[PK]): ConnectionIO[Boolean] =
    existsAtLeastOneSQLFragment(pks.toList).query[Boolean].unique

  override def existAll(pks: Iterable[PK]): ConnectionIO[Boolean] =
    existsAllSQLFragment(pks.toList).query[Boolean].unique

  //----- plain string queries -----
  private val findSQL: String =
    s"SELECT ${table.row.sql.tuple} FROM ${table.name} WHERE ${table.row.pkColumn} = ?"

  private val insertSQL: String =
    s"INSERT INTO ${table.name} ${table.row.sql.tupleInParens} VALUES ${table.row.sql.qmsInParens}"

  /**
    * Generate something like:
    * {{{
    *
    * }}}
    */
  private val updateSQL: String =
    s"""
       |UPDATE ${table.name} SET ${table.row.sql.tupleEqualQM}
       |WHERE ${table.row.pkColumn} = ?
       |""".stripMargin

  private val deleteSQL: String =
    s"DELETE FROM ${table.name} WHERE ${table.row.pkColumn} = ?"

  private val existsSQL =
    s"SELECT EXISTS(SELECT 1 FROM ${table.name} WHERE ${table.row.pkColumn} = ?)"

  //----- fragments -----

  private def deleteManySQLFragment(pks: List[PK]): Fragment =
    Fragment.const(s"DELETE FROM ${table.name} WHERE ${table.row.pkColumn}") ++ frags.inArray(pks)

  private def existsAtLeastOneSQLFragment(pks: List[PK]): Fragment =
    Fragment.const(s"SELECT EXISTS(SELECT 1 FROM ${table.name} WHERE ${table.row.pkColumn}") ++
      frags.inArray(pks) ++ fr")"

  private def existsAllSQLFragment(pks: List[PK]): Fragment =
    Fragment.const(s"SELECT (SELECT COUNT(*) FROM ${table.name} WHERE ${table.row.pkColumn}") ++
      frags.inArray(pks) ++ fr")" ++ Fragment.const(s" = ${pks.size}")

}
