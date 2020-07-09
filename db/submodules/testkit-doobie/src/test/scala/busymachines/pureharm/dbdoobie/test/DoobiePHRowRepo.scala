/**
  * Copyright (c) 2017-2019 BusyMachines
  *
  * See company homepage at: https://www.busymachines.com/
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package busymachines.pureharm.dbdoobie.test

import busymachines.pureharm.effects._
import busymachines.pureharm.db._
import busymachines.pureharm.db.testdata._
import busymachines.pureharm.dbdoobie._

/**
  * Use this also as a rough outline of how you ought to structure
  * your code. Create a trait that extends the
  * [[Repo[F, MyCaseClass, MyPrimaryKeyType]]]
  * add whatever new methods/override the default ones you need here.
  *
  * Implement the queries in terms of ConnectionIO similar to
  * [[DoobiePHRowRepo.DoobiePHRowQueries]]
  *
  * and the final DAO in IO similar to
  * [[DoobiePHRowRepo.DoobiePHRowRepoImpl]]
  *
  * Voila! Bunch of free CRUD! + a lot of helpers to build
  * common queries in the [[DoobiePHRowRepo.DoobieDoobiePHRowTable]]
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 24 Sep 2019
  */
private[test] trait DoobiePHRowRepo[F[_]] extends PHRowRepo[F]

private[test] object DoobiePHRowRepo {

  def apply[F[_]: BracketAttempt](trans: Transactor[F]): DoobiePHRowRepo[F] = {
    implicit val i: Transactor[F] = trans
    new DoobiePHRowRepoImpl[F]
  }

  //----------------- implementation details -----------------
  import busymachines.pureharm.dbdoobie.implicits._
  import busymachines.pureharm.json._

  object DoobieDoobiePHRowTable extends TableWithPK[PHRow, PhantomPK] {
    override val name: TableName = schema.PureharmRows

    val byte_col:      Column = createColumn("byte")
    val int_col:       Column = createColumn("int")
    val long_col:      Column = createColumn("long")
    val big_decimal:   Column = createColumn("big_decimal")
    val string_col:    Column = createColumn("string")
    val jsonb_col:     Column = createColumn("jsonb_col")
    val opt_col:       Column = createColumn("opt_col")
    val unique_string: Column = createColumn("unique_string")
    val unique_int:    Column = createColumn("unique_int")
    val unique_json:   Column = createColumn("unique_json")

    implicit val jsonCodec: Codec[PHJSONCol] = derive.codec[PHJSONCol]

    override val showPK: Show[PhantomPK] = Show[PhantomPK]
    override val metaPK: Meta[PhantomPK] = Meta[PhantomPK]
    override val readE:  Read[PHRow]     = Read[PHRow]
    override val writeE: Write[PHRow]    = Write[PHRow]
  }

  final private object DoobiePHRowQueries
    extends DoobieRepoQueries[PHRow, PhantomPK, DoobieDoobiePHRowTable.type] with DoobiePHRowRepo[ConnectionIO] {
    override def table: DoobieDoobiePHRowTable.type = DoobieDoobiePHRowTable
  }

  final private class DoobiePHRowRepoImpl[F[_]: BracketAttempt](implicit
    override val transactor: Transactor[F]
  ) extends DoobieRepo[F, PHRow, PhantomPK, DoobieDoobiePHRowTable.type] with DoobiePHRowRepo[F] {
    override protected val queries: DoobiePHRowQueries.type = DoobiePHRowQueries
  }
}
