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
package busymachines.pureharm.dbslick.test

import busymachines.pureharm.db.testdata._
import testdb._

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 12 Jun 2019
  */
private[test] trait SlickPHRTestRepo[F[_]] extends PHRTestRepo[F]

private[test] object SlickPHRTestRepo {

  def apply[F[_]: Transactor](implicit ec: ConnectionIOEC): SlickPHRTestRepo[F] =
    new SlickPHRTestRepoImpl[F]

  //----------------- implementation details -----------------
  import testdb.implicits._

  //---------------- json stuff ---------------
  import busymachines.pureharm.json._

  implicit val pureharmJSONCol:        Codec[PHJSONCol]      = derive.codec[PHJSONCol]
  implicit private val jsonColumnType: ColumnType[PHJSONCol] = createJsonbColumnType[PHJSONCol]

  implicit private val uniqueJsonColumnType: ColumnType[UniqueJSON] =
    jsonColumnType.asInstanceOf[ColumnType[UniqueJSON]] //TODO: implement map operations on column types...

  private class SlickPHRTestTable(tag: Tag) extends TableWithPK[PHRow, PhantomPK](tag, schema.PureharmRows) {
    val byte         = column[PhantomByte]("byte")
    val int          = column[PhantomInt]("int")
    val long         = column[PhantomLong]("long")
    val bigDecimal   = column[PhantomBigDecimal]("big_decimal")
    val string       = column[PhantomString]("string")
    val jsonCol      = column[PHJSONCol]("jsonb_col")
    val optCol       = column[Option[PhantomString]]("opt_col")
    val uniqueString = column[UniqueString]("unique_string")
    val uniqueInt    = column[UniqueInt]("unique_int")
    val uniqueJSON   = column[UniqueJSON]("unique_json")

    override def * : ProvenShape[PHRow] =
      (
        id,
        byte,
        int,
        long,
        bigDecimal,
        string,
        jsonCol,
        optCol,
        uniqueString,
        uniqueInt,
        uniqueJSON,
      ) <> ((PHRow.apply _).tupled, PHRow.unapply)
  }

  final private class SlickPHRTestQueries(implicit
    override val connectionIOEC: ConnectionIOEC
  ) extends SlickRepoQueries[PHRow, PhantomPK, SlickPHRTestTable] with SlickPHRTestRepo[ConnectionIO] {
    override val dao: TableQuery[SlickPHRTestTable] = TableQuery[SlickPHRTestTable]
  }

  final private class SlickPHRTestRepoImpl[F[_]](
    implicit override val connectionIOEC: ConnectionIOEC,
    implicit override val transactor:     Transactor[F],
  ) extends SlickRepo[F, PHRow, PhantomPK, SlickPHRTestTable] with SlickPHRTestRepo[F] {
    override protected val queries: SlickPHRTestQueries = new SlickPHRTestQueries
  }
}
