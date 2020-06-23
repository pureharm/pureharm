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
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 24 Sep 2019
  *
  */
private[test] object DoobiePureharmRowDAO {

  def apply[F[_]: Transactor: BracketAttempt]: PureharmRowDAO[F] =
    new PureharmRowDAODoobieImpl[F]

  //----------------- implementation details -----------------
  import busymachines.pureharm.dbdoobie.implicits._
  import busymachines.pureharm.json._

  implicit val pureharmJSONCol: Codec[PureharmJSONCol] = derive.codec[PureharmJSONCol]

  private class DoobiePureharmTable extends TableWithPK[PureharmRow, PhantomPK] {

    override val tableName: TableName = schema.PureharmRows

    override val tailColumns: List[ColumnName] = List(
      ColumnName("byte"),
      ColumnName("int"),
      ColumnName("long"),
      ColumnName("big_decimal"),
      ColumnName("string"),
      ColumnName("jsonb_col"),
      ColumnName("opt_col"),
    )
  }

  final private class DoobiePureharmRowQuerries(
    override val table: DoobiePureharmTable
  ) extends DoobieQueryAlgebra[PureharmRow, PhantomPK, DoobiePureharmTable] {
    override val getPK: Get[PhantomPK] = Get[PhantomPK]
    override val putPK: Put[PhantomPK] = Put[PhantomPK]

    override val readE: Read[PureharmRow]  = Read[PureharmRow]
    override val writeE: Write[PureharmRow] = Write[PureharmRow]

    override val showPK: Show[PhantomPK] = Show[PhantomPK]

  }

  final private class PureharmRowDAODoobieImpl[F[_]: BracketAttempt](implicit
    override val transactor: Transactor[F]
  ) extends DoobieDAOAlgebra[F, PureharmRow, PhantomPK, DoobiePureharmTable] with PureharmRowDAO[F] {
    override protected val queries: DoobiePureharmRowQuerries = new DoobiePureharmRowQuerries(new DoobiePureharmTable())
  }
}
