/** Copyright (c) 2017-2019 BusyMachines
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

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 12 Jun 2019
  */
private[test] trait SlickExtPHRowRepo[F[_]] extends Repo[F, ExtPHRow, PhantomUUID]

private[test] object SlickExtPHRowRepo {

  def apply[F[_]: Transactor](implicit ec: ConnectionIOEC): SlickExtPHRowRepo[F] =
    new SlickExtPHRowRepoImpl[F]

  //----------------- implementation details -----------------
  import testdb.implicits._

  private class SlickExtPHRowTable(tag: Tag)
    extends TableWithPK[ExtPHRow, PhantomUUID](tag, schema.PureharmExternalRows) {
    val row_id = column[PhantomPK]("row_id")

    override def * : ProvenShape[ExtPHRow] =
      (id, row_id).<>((ExtPHRow.apply _).tupled, ExtPHRow.unapply)
  }

  final private class SlickExtPHRowQueries(implicit
    override val connectionIOEC: ConnectionIOEC
  ) extends SlickRepoQueries[ExtPHRow, PhantomUUID, SlickExtPHRowTable] with SlickExtPHRowRepo[ConnectionIO] {
    override val dao: TableQuery[SlickExtPHRowTable] = TableQuery[SlickExtPHRowTable]
  }

  final private class SlickExtPHRowRepoImpl[F[_]](
    implicit override val connectionIOEC: ConnectionIOEC,
    implicit override val transactor:     Transactor[F],
  ) extends SlickRepo[F, ExtPHRow, PhantomUUID, SlickExtPHRowTable] with SlickExtPHRowRepo[F] {
    override protected val queries: SlickExtPHRowQueries = new SlickExtPHRowQueries
  }
}
