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

import java.util.UUID

import busymachines.pureharm.effects._
import busymachines.pureharm.db._
import busymachines.pureharm.db.testdata._
import busymachines.pureharm.dbdoobie._

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 03 Jul 2020
  */
private[test] trait DoobieExtPHRowRepo[F[_]] extends Repo[F, ExtPHRow, PhantomUUID]

private[test] object DoobieExtPHRowRepo {

  def apply[F[_]: BracketAttempt](trans: Transactor[F]): DoobieExtPHRowRepo[F] = {
    implicit val i: Transactor[F] = trans
    new DoobiePHRTestRepoImpl[F]
  }

  //----------------- implementation details -----------------
  import busymachines.pureharm.dbdoobie.implicits._

  object DoobiePHExtRowTable extends TableWithPK[ExtPHRow, PhantomUUID] {
    override val name: TableName = schema.PureharmExternalRows
    val row_id:        Column    = createColumn("row_id")

    implicit override val metaPK: Meta[PhantomUUID] = Meta[UUID].imap(PhantomUUID.spook)(PhantomUUID.despook)
    override val showPK:          Show[PhantomUUID] = Show[PhantomUUID]
    override val readE:           Read[ExtPHRow]    = Read[ExtPHRow]
    override val writeE:          Write[ExtPHRow]   = Write[ExtPHRow]
  }

  final private object DoobieExtPHRowQueries
    extends DoobieRepoQueries[ExtPHRow, PhantomUUID, DoobiePHExtRowTable.type] with DoobieExtPHRowRepo[ConnectionIO] {
    override def table: DoobiePHExtRowTable.type = DoobiePHExtRowTable
  }

  final private class DoobiePHRTestRepoImpl[F[_]: BracketAttempt](implicit
    override val transactor: Transactor[F]
  ) extends DoobieRepo[F, ExtPHRow, PhantomUUID, DoobiePHExtRowTable.type] with DoobieExtPHRowRepo[F] {
    override protected val queries: DoobieExtPHRowQueries.type = DoobieExtPHRowQueries
  }
}
