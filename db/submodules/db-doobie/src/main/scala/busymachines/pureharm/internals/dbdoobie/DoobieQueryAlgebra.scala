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
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 24 Sep 2019
  *
  */
abstract class DoobieQueryAlgebra[E, PK, Table <: TableWithPK[E, PK]] extends DAOAlgebra[ConnectionIO, E, PK] {
  def table: Table

  implicit def getPK: Get[PK]
  implicit def putPK: Put[PK]

  implicit def getE: Read[E]
  implicit def putE: Write[E]

  protected def pkColumn:    Fragment = table.pkColumn
  protected def tableName:   Fragment = fr"${table.tableName: String}"
  protected def tuple:       Fragment = table.tuple
  protected def tupleParens: Fragment = table.tupleParens

  override def find(pk: PK): ConnectionIO[Option[E]] = {
    val frag: Fragment = fr"SELECT" ++ tuple ++ fr"FROM" ++ tableName ++ fr"WHERE" ++ table.pkColumn ++ fr"= $pk"
    frag.query[E].option
  }

  override def retrieve(pk: PK)(implicit show: Show[PK]): ConnectionIO[E] =
    this.find(pk).flattenOption(DoobieDBEntryNotFoundAnomaly(pk.show, Option.empty))

  override def insert(e: E): ConnectionIO[PK] = {
    ???
//    val frag: Fragment = fr"INSERT INTO" ++ tupleParens ++ fr"VALUES $e"
//    frag.update.withUniqueGeneratedKeys[PK](table.pkFieldName)
  }

  override def insertMany(es: Iterable[E]): ConnectionIO[Unit] = ???

  override def update(e: E): ConnectionIO[E] = ???

  override def updateMany[M[_]: Traverse](es: M[E]): ConnectionIO[Unit] = ???

  override def delete(pk: PK): ConnectionIO[Unit] = ???

  override def deleteMany(pks: Iterable[PK]): ConnectionIO[Unit] = ???

  override def exists(pk: PK): ConnectionIO[Boolean] = ???

  override def existsAtLeastOne(pks: Iterable[PK]): ConnectionIO[Boolean] = ???

  override def existAll(pks: Iterable[PK]): ConnectionIO[Boolean] = ???
}
