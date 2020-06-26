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

import busymachines.pureharm.db._
import busymachines.pureharm.dbslick._
import busymachines.pureharm.db.test._
import busymachines.pureharm.db.testkit.PHTDBConfig
import busymachines.pureharm.effects._
import busymachines.pureharm.testkit._

/**
  * @author Daniel Incicau, daniel.incicau@busymachines.com
  * @since 27/01/2020
  */
final class SlickTransactorTest extends FixturePureharmTest {

  private lazy val slickConfig: SlickDBIOAsyncExecutorConfig = SlickDBIOAsyncExecutorConfig.default

  private lazy val connectionConfig: DBConnectionConfig = PHTDBConfig.dbConfig.copy(
    schema = PHTDBConfig.schemaName("slick_transactor")
  )

  /**
    * Instead of the "before and after shit" simply init, and close
    * everything in this Resource...
    */
  override def fixture(meta: MetaData): Resource[IO, Transactor[IO]] =
    for {
      dbConfig   <- Resource.pure[IO, DBConnectionConfig](connectionConfig)
      transactor <-
        Transactor
          .pgSQLHikari[IO](dbProfile = testdb.jdbcProfileAPI, dbConnection = dbConfig, asyncConfig = slickConfig)
    } yield transactor

  override type FixtureParam = Transactor[IO]

  test("creates the transactor and the session connection is open from the start") { implicit trans: Transactor[IO] =>
    for {
      isConnected <- trans.isConnected
    } yield assert(isConnected === true)
  }

  test("closes the active connection") { implicit trans: Transactor[IO] =>
    for {
      isConnected <- trans.isConnected
      _ = assert(isConnected === true)
      _           <- trans.closeSession
      isConnected <- trans.isConnected
    } yield assert(isConnected === false)
  }

  test("recreates the connection when the current connection is open") { implicit trans: Transactor[IO] =>
    for {
      isConnected <- trans.isConnected
      _ = assert(isConnected === true)
      _           <- trans.recreateSession
      isConnected <- trans.isConnected
    } yield assert(isConnected === true)
  }

  test("recreates the connection when the current connection is closed") { implicit trans: Transactor[IO] =>
    for {
      isConnected <- trans.isConnected
      _ = assert(isConnected === true)
      _           <- trans.closeSession
      isConnected <- trans.isConnected
      _ = assert(isConnected === false)
      _           <- trans.recreateSession
      isConnected <- trans.isConnected
    } yield assert(isConnected === true)
  }

}
