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
import busymachines.pureharm.db.test._
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
  * [[DoobiePureharmRowDAO.DoobiePureharmRowQueries]]
  *
  * and the final DAO in IO similar to
  * [[DoobiePureharmRowDAO.DoobiePureharmRowDAOImpl]]
  *
  * Voila! Bunch of free CRUD! + a lot of helpers to build
  * common queries in the [[DoobiePureharmRowDAO.DoobiePureharmTable]]
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 24 Sep 2019
  *
  */
private[test] trait DoobiePureharmRowDAO[F[_]] extends PureharmRowDAO[F]

private[test] object DoobiePureharmRowDAO {

  def apply[F[_]: Transactor: BracketAttempt]: DoobiePureharmRowDAO[F] =
    new DoobiePureharmRowDAOImpl[F]

  //----------------- implementation details -----------------
  import busymachines.pureharm.dbdoobie.implicits._
  import busymachines.pureharm.json._

  object DoobiePureharmTable extends TableWithPK[PureharmRow, PhantomPK] {
    override val name: TableName = schema.PureharmRows

    val byte_col:    ColumnName = ColumnName("byte")
    val int_col:     ColumnName = ColumnName("int")
    val long_col:    ColumnName = ColumnName("long")
    val big_decimal: ColumnName = ColumnName("big_decimal")
    val string_col:  ColumnName = ColumnName("string")
    val jsonb_col:   ColumnName = ColumnName("jsonb_col")
    val opt_col:     ColumnName = ColumnName("opt_col")

    implicit private[DoobiePureharmRowDAO] val pureharmJSONColMeta: Meta[PureharmJSONCol] =
      jsonMeta[PureharmJSONCol](derive.codec[PureharmJSONCol])

    override val showPK: Show[PhantomPK]    = Show[PhantomPK]
    override val metaPK: Meta[PhantomPK]    = Meta[PhantomPK]
    override val readE:  Read[PureharmRow]  = Read[PureharmRow]
    override val writeE: Write[PureharmRow] = Write[PureharmRow]
  }

  final private object DoobiePureharmRowQueries
    extends DoobieQueryAlgebra[PureharmRow, PhantomPK, DoobiePureharmTable.type]
    with DoobiePureharmRowDAO[ConnectionIO] {
    override def table: DoobiePureharmTable.type = DoobiePureharmTable

    //implement queries from PureharmRowDAO here
  }

  final private class DoobiePureharmRowDAOImpl[F[_]: BracketAttempt](implicit
    override val transactor: Transactor[F]
  ) extends DoobieDAOAlgebra[F, PureharmRow, PhantomPK, DoobiePureharmTable.type] with DoobiePureharmRowDAO[F] {
    override protected val queries: DoobiePureharmRowQueries.type = DoobiePureharmRowQueries

    //lift queries from DoobiePureharmRowQueries here
  }
}
