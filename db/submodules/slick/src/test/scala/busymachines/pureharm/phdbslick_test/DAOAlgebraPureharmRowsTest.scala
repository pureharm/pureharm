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
package busymachines.pureharm.phdbslick_test

import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._
import busymachines.pureharm.db_test._
import busymachines.pureharm.db._
import busymachines.pureharm.dbslick._

/**
  * To properly run this test, you probably want to start the
  * PostgreSQL server inside docker using the following script:
  * {{{
  *   ./db/docker-pureharm-postgresql-test.sh
  * }}}
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 12 Jun 2019
  *
  */
final class DAOAlgebraPureharmRowsTest extends PureharmFixtureTest {
  override type FixtureParam = PureharmRowDAO[IO]

  override def fixture: Resource[IO, PureharmRowDAO[IO]] =
    DAOAlgebraPureharmRowsTest.transactorResource.map(
      implicit t => SlickPureharmRowDAO[IO](t, ConnectionIOEC(executionContext)),
    )

  iotest("write single row + read by PK") { implicit dao: PureharmRowDAO[IO] =>
    val row = PureharmRow(
      id         = PhantomPK("test1"),
      byte       = PhantomByte(245.toByte),
      int        = PhantomInt(41),
      long       = PhantomLong(0.toLong),
      bigDecimal = PhantomBigDecimal(BigDecimal("1390749832749238")),
      string     = PhantomString("first_test_in_a_while"),
    )

    for {
      _          <- dao.insert(row)
      fetchedRow <- dao.find(row.id).flattenOption(fail(s"PK=${row.id} row was not in database"))
    } yield assert(row === fetchedRow)
  }
}

private[phdbslick_test] object DAOAlgebraPureharmRowsTest {

  /**
    * All these values come from this file:
    * db/docker-pureharm-postgresql-test.sh
    *
    */
  private val LocalPort    = 20010
  private val LocalHost    = "localhost"
  private val TestDBName   = DatabaseName("pureharm_test")
  private val PureharmUser = DBUsername("pureharmony")
  private val PureharmPwd  = DBPassword("pureharmony")
  private val url          = JDBCUrl.postgresql(LocalPort, LocalHost, TestDBName)

  def transactorResource(implicit cs: ContextShift[IO]): Resource[IO, Transactor[IO]] = {
    val trans = Transactor.pgSQLHikari[IO](db.jdbcProfileAPI)(
      url      = url,
      username = PureharmUser,
      password = PureharmPwd,
      config   = SlickDBIOAsyncExecutorConfig.default,
    )

    //no cleanup afterwards because the transactor is shutdown before a next flatMap
    cleanDB >> initDB >> trans
  }

  private def initDB: Resource[IO, Unit] = Resource.liftF[IO, Unit] {
    for {
      _ <- Flyway.migrate[IO](
        url                = url,
        username           = PureharmUser,
        password           = PureharmPwd,
        migrationLocations = List.empty,
      )
    } yield ()
  }

  private def cleanDB: Resource[IO, Unit] = Resource.liftF[IO, Unit] {
    for {
      _ <- Flyway.clean[IO](
        url      = url,
        username = PureharmUser,
        password = PureharmPwd,
      )
    } yield ()
  }
}
