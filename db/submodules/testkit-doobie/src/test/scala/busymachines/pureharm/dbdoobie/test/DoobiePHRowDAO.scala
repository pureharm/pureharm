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
import busymachines.pureharm.db.testdata.{PHTDAO, PHTJSONCol, PHTRow}
import busymachines.pureharm.dbdoobie._
import busymachines.pureharm.internals.dbdoobie._

/**
  *
  * Use this also as a rough outline of how you ought to structure
  * your code. Create a trait that extends the
  * [[DAOAlgebra[F, MyCaseClass, MyPrimaryKeyType]]]
  * add whatever new methods/override the default ones you need here.
  *
  * Implement the queries in terms of ConnectionIO similar to
  * [[DoobiePHRowDAO.DoobiePureharmRowQueries]]
  *
  * and the final DAO in IO similar to
  * [[DoobiePHRowDAO.DoobiePureharmRowDAOImpl]]
  *
  * Voila! Bunch of free CRUD! + a lot of helpers to build
  * common queries in the [[DoobiePHRowDAO.DoobiePureharmTable]]
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 24 Sep 2019
  *
  */
private[test] trait DoobiePHRowDAO[F[_]] extends PHTDAO[F]

private[test] object DoobiePHRowDAO {

  def apply[F[_]: BracketAttempt](trans: Transactor[F]): DoobiePHRowDAO[F] = {
    implicit val i: Transactor[F] = trans
    new DoobiePureharmRowDAOImpl[F]
  }

  //----------------- implementation details -----------------
  import busymachines.pureharm.dbdoobie.implicits._
  import busymachines.pureharm.json._

  object DoobiePureharmTable extends TableWithPK[PHTRow, PhantomPK] {
    override val name: TableName = schema.PureharmRows

    val byte_col:    Column = createColumn("byte")
    val int_col:     Column = createColumn("int")
    val long_col:    Column = createColumn("long")
    val big_decimal: Column = createColumn("big_decimal")
    val string_col:  Column = createColumn("string")
    val jsonb_col:   Column = createColumn("jsonb_col")
    val opt_col:     Column = createColumn("opt_col")

    implicit private[DoobiePHRowDAO] val pureharmJSONColMeta: Meta[PHTJSONCol] =
      jsonMeta[PHTJSONCol](derive.codec[PHTJSONCol])

    override val showPK: Show[PhantomPK] = Show[PhantomPK]
    override val metaPK: Meta[PhantomPK] = Meta[PhantomPK]
    override val readE:  Read[PHTRow]    = Read[PHTRow]
    override val writeE: Write[PHTRow]   = Write[PHTRow]
  }

  final private object DoobiePureharmRowQueries
    extends DoobieQueryAlgebra[PHTRow, PhantomPK, DoobiePureharmTable.type] with DoobiePHRowDAO[ConnectionIO] {
    override def table: DoobiePureharmTable.type = DoobiePureharmTable
  }

  final private class DoobiePureharmRowDAOImpl[F[_]: BracketAttempt](implicit
    override val transactor: Transactor[F]
  ) extends DoobieDAOAlgebra[F, PHTRow, PhantomPK, DoobiePureharmTable.type] with DoobiePHRowDAO[F] {
    override protected val queries: DoobiePureharmRowQueries.type = DoobiePureharmRowQueries
  }
}
