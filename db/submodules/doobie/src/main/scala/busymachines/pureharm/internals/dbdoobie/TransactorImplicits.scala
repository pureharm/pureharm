/** Copyright (c) 2019 BusyMachines
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

import busymachines.pureharm.db._
import busymachines.pureharm.dbdoobie._
import busymachines.pureharm.effects._
import doobie.hikari.HikariTransactor

/** There is little point in using something other than a
  * Hikari Transactor for this...
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 26 Jun 2020
  */
trait TransactorImplicits {

  implicit class TransStuff(t: Transactor.type) {

    def pureharmTransactor[F[_]: Async: ContextShift](
      dbConfig:  DBConnectionConfig,
      dbConnEC:  DoobieConnectionEC,
      dbBlocker: DoobieBlocker,
    ): Resource[F, Transactor[F]] =
      TransactorImplicits.pureharmTransactor[F](dbConfig, dbConnEC, dbBlocker)
  }

}

private[pureharm] object TransactorImplicits {

  def pureharmTransactor[F[_]: Async: ContextShift](
    dbConfig:  DBConnectionConfig,
    dbConnEC:  DoobieConnectionEC,
    dbBlocker: DoobieBlocker,
  ): Resource[F, Transactor[F]] =
    for {
      transactor <- HikariTransactor.newHikariTransactor(
        driverClassName = "org.postgresql.Driver",
        url             = dbConfig.jdbcURL,
        user            = dbConfig.username,
        pass            = dbConfig.password,
        connectEC       = dbConnEC,
        blocker         = dbBlocker,
      )
    } yield transactor
}
