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
package busymachines.pureharm.dbslick.testkit

import busymachines.pureharm.db._
import busymachines.pureharm.db.testkit._
import busymachines.pureharm.dbslick._
import busymachines.pureharm.effects._
import busymachines.pureharm.testkit._
import busymachines.pureharm.testkit.util.MDCKeys
import org.scalatest._

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 26 Jun 2020
  */
abstract class SlickDBTestSetup(private val dbProfile: JDBCProfileAPI) extends DBTestSetup[Transactor[IO]] {

  /** Should be overridden to create a connection config appropriate for the test
    */
  override def dbConfig(meta: TestData)(implicit logger: TestLogger): DBConnectionConfig

  override def dbTransactorInstance(
    meta:        TestData
  )(implicit rt: RT, logger: TestLogger): Resource[IO, Transactor[IO]] = {
    val config = dbConfig(meta)
    import rt._
    for {
      _     <- logger.info(MDCKeys(meta))(s"CREATING Transactor[IO] for: ${config.jdbcURL}").to[Resource[IO, *]]
      trans <- Transactor.pgSQLHikari[IO](
        dbProfile    = dbProfile,
        dbConnection = config,
        asyncConfig  = SlickDBIOAsyncExecutorConfig.default,
      )
    } yield trans
  }
}
