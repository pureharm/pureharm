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
package busymachines.pureharm.dbslick.psql.test

import busymachines.pureharm.db.testdata._
import busymachines.pureharm.dbslick.psql.test.testdb._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 12 Jun 2019
  *
  */
private[test] trait SlickPureharmRowDAO[F[_]] extends PHTDAO[F]

private[test] object SlickPureharmRowDAO {

  def apply[F[_]: Transactor](implicit ec: ConnectionIOEC): SlickPureharmRowDAO[F] =
    new PureharmRowDAOSlickImpl[F]

  //----------------- implementation details -----------------
  import testdb.implicits._

  //---------------- json stuff ---------------
  import busymachines.pureharm.json._

  implicit private val pureharmJSONCol: Codec[PHTJSONCol]      = derive.codec[PHTJSONCol]
  implicit private val jsonColumnType:  ColumnType[PHTJSONCol] = createJsonbColumnType[PHTJSONCol]

  private class SlickPureharmTable(tag: Tag) extends TableWithPK[PHTRow, PhantomPK](tag, schema.PureharmRows) {
    val byte       = column[PhantomByte]("byte")
    val int        = column[PhantomInt]("int")
    val long       = column[PhantomLong]("long")
    val bigDecimal = column[PhantomBigDecimal]("big_decimal")
    val string     = column[PhantomString]("string")
    val jsonCol    = column[PHTJSONCol]("jsonb_col")
    val optCol     = column[Option[PhantomString]]("opt_col")

    override def * : ProvenShape[PHTRow] =
      (id, byte, int, long, bigDecimal, string, jsonCol, optCol) <> ((PHTRow.apply _).tupled, PHTRow.unapply)
  }

  final private class SlickPureharmRowQuerries(implicit
    override val connectionIOEC: ConnectionIOEC
  ) extends SlickDAOQueryAlgebra[PHTRow, PhantomPK, SlickPureharmTable] with SlickPureharmRowDAO[ConnectionIO] {
    override val dao: TableQuery[SlickPureharmTable] = TableQuery[SlickPureharmTable]
  }

  final private class PureharmRowDAOSlickImpl[F[_]](
    implicit override val connectionIOEC: ConnectionIOEC,
    implicit override val transactor:     Transactor[F],
  ) extends SlickDAOAlgebra[F, PHTRow, PhantomPK, SlickPureharmTable] with SlickPureharmRowDAO[F] {
    override protected val queries: SlickPureharmRowQuerries = new SlickPureharmRowQuerries
  }
}
