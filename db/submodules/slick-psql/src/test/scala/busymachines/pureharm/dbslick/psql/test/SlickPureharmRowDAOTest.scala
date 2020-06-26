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

import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._
import busymachines.pureharm.db.test._
import busymachines.pureharm.db._
import busymachines.pureharm.db.testkit._
import busymachines.pureharm.dbslick._
import busymachines.pureharm.testkit._

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
final class SlickPureharmRowDAOTest extends FixturePureharmTest {
  override type FixtureParam = PHTDAO[IO]

  override def fixture(meta: MetaData): Resource[IO, PHTDAO[IO]] =
    SlickPureharmRowDAOTest
      .transactorResource[IO]
      .map(implicit t => SlickPureharmRowDAO[IO](t, ConnectionIOEC(executionContext)))

  private val row = PHTRow(
    id          = PhantomPK("test1"),
    byte        = PhantomByte(245.toByte),
    int         = PhantomInt(41),
    long        = PhantomLong(0.toLong),
    bigDecimal  = PhantomBigDecimal(BigDecimal("1390749832749238")),
    string      = PhantomString("first_test_in_a_while"),
    jsonbCol    = PHTJSONCol(42, "json_column"),
    optionalCol = Option(PhantomString("optional_value")),
  )

  test("insert + find") { implicit dao: PHTDAO[IO] =>
    for {
      _          <- dao.insert(row)
      fetchedRow <- dao.find(row.id).flattenOption(fail(s"PK=${row.id} row was not in database"))
    } yield assert(row === fetchedRow)
  }

  test("insert + retrieve") { implicit dao: PHTDAO[IO] =>
    for {
      _          <- dao.insert(row)
      fetchedRow <- dao.retrieve(row.id)
    } yield assert(row === fetchedRow)
  }

  test("insert + update (defined opt) + retrieve") { implicit dao: PHTDAO[IO] =>
    for {
      _ <- dao.insert(row)
      newRowWithSome = row.copy(
        byte        = PhantomByte(111.toByte),
        int         = PhantomInt(42),
        long        = PhantomLong(6.toLong),
        bigDecimal  = PhantomBigDecimal(BigDecimal("328572")),
        string      = PhantomString("updated_string"),
        jsonbCol    = PHTJSONCol(79, "new_json_col"),
        optionalCol = Option(PhantomString("new opt_value")),
      )
      _ <- dao.update(newRowWithSome)
      fetchedRow <- dao.retrieve(row.id)
    } yield assert(newRowWithSome === fetchedRow)
  }

  test("insert + update (nulled opt) + retrieve") { implicit dao: PHTDAO[IO] =>
    for {
      _ <- dao.insert(row)
      newRowWithNone = row.copy(
        byte        = PhantomByte(56.toByte),
        int         = PhantomInt(13),
        long        = PhantomLong(8.toLong),
        bigDecimal  = PhantomBigDecimal(BigDecimal("124325671")),
        string      = PhantomString("second_updated_string"),
        jsonbCol    = PHTJSONCol(45, "newest_json_col"),
        optionalCol = Option.empty,
      )
      _ <- dao.update(newRowWithNone)
      fetchedRow <- dao.retrieve(row.id)
    } yield assert(newRowWithNone === fetchedRow)
  }

  test("failed retrieve") { implicit dao: PHTDAO[IO] =>
    for {
      att <- dao.retrieve(PhantomPK("sdfsdlksld")).attempt
    } yield assertThrows[DBEntryNotFoundAnomaly](
      att.unsafeGet()
    ) //FIXME: create assertSuccess/assertFailure combinator
  }
}

private[test] object SlickPureharmRowDAOTest {

  /**
    * We do this to not conflict with the doobie test if run in parallel
    * FIXME: use a unique schema for each test case! That way we can
    * FIXME: fully paralelize DB tests, have to work around
    */
  private val dbConfig: DBConnectionConfig = PHTDBConfig.dbConfig.copy(
    schema = PHTDBConfig.schemaName("slick")
  )

  def transactorResource[F[_]: Concurrent: ContextShift]: Resource[F, Transactor[F]] = {
    val trans = Transactor.pgSQLHikari[F](
      dbProfile    = testdb.jdbcProfileAPI,
      dbConnection = dbConfig,
      asyncConfig  = SlickDBIOAsyncExecutorConfig.default,
    )

    //no cleanup afterwards because the transactor is shutdown before a next flatMap
    cleanDB >> initDB >> trans
  }

  private def initDB[F[_]: Sync]: Resource[F, Unit] = Resource.liftF[F, Unit] {
    for {
      _ <- flyway.Flyway.migrate[F](dbConfig = dbConfig, Option.empty)
    } yield ()
  }

  private def cleanDB[F[_]: Sync]: Resource[F, Unit] = Resource.liftF[F, Unit] {
    for {
      _ <- flyway.Flyway.clean[F](dbConfig)
    } yield ()
  }
}
