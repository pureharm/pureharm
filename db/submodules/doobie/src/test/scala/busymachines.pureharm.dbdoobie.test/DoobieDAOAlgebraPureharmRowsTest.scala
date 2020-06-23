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
import busymachines.pureharm.effects.implicits._
import busymachines.pureharm.db.test.{PhantomPK, _}
import busymachines.pureharm.db._
import busymachines.pureharm.dbdoobie._

/**
  * To properly run this test, you probably want to start the
  * PostgreSQL server inside docker using the following script:
  * {{{
  *   ./db/docker-pureharm-postgresql-test.sh
  * }}}
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 24 Sept 2019
  *
  */
final class DoobieDAOAlgebraPureharmRowsTest extends PureharmFixtureTest {
  override type FixtureParam = DoobiePureharmRowDAO[IO]

  override def fixture: Resource[IO, FixtureParam] =
    DoobieDAOAlgebraPureharmRowsTest
      .transactorResource[IO]
      .map(implicit t => DoobiePureharmRowDAO[IO])

  private val row1 = PureharmRow(
    id          = PhantomPK("row1_id"),
    byte        = PhantomByte(245.toByte),
    int         = PhantomInt(41),
    long        = PhantomLong(0.toLong),
    bigDecimal  = PhantomBigDecimal(BigDecimal("1390749832749238")),
    string      = PhantomString("row1_string"),
    jsonbCol    = PureharmJSONCol(42, "row1_json_column"),
    optionalCol = Option(PhantomString("row1_optional_value")),
  )

  private val row2 = PureharmRow(
    id          = PhantomPK("row2_id"),
    byte        = PhantomByte(123.toByte),
    int         = PhantomInt(4321),
    long        = PhantomLong(12L),
    bigDecimal  = PhantomBigDecimal(BigDecimal("23414")),
    string      = PhantomString("row2_string"),
    jsonbCol    = PureharmJSONCol(44, "row2_json_column"),
    optionalCol = Option(PhantomString("row2_optional_value")),
  )

  test("find — none") { implicit dao: PureharmRowDAO[IO] =>
    for {
      att <- dao.find(PhantomPK("sdfsdlksld"))
    } yield att match {
      case None    => succeed
      case Some(v) => fail(s"should have been none, but got: $v")
    }
  }

  test("retrieve — failed") { implicit dao: PureharmRowDAO[IO] =>
    for {
      att <- dao.retrieve(PhantomPK("sdfsdlksld")).attempt
    } yield att match {
      case Left(err) => err shouldBe a[DBEntryNotFoundAnomaly]
      case Right(v)  => fail(s"should have failed, but got: $v")
    }
  }

  test("exists — not") { implicit dao: PureharmRowDAO[IO] =>
    for {
      exists <- dao.exists(row1.id)
    } yield assert(!exists)
  }

  test("insert + find") { implicit dao: PureharmRowDAO[IO] =>
    for {
      _          <- dao.insert(row1)
      fetchedRow <- dao.find(row1.id).flattenOption(fail(s"PK=${row1.id} row was not in database"))
    } yield assert(row1 === fetchedRow)
  }

  test("insert + retrieve") { implicit dao: PureharmRowDAO[IO] =>
    for {
      _          <- dao.insert(row1)
      fetchedRow <- dao.retrieve(row1.id)
    } yield assert(row1 === fetchedRow)
  }

  test("insert + exists") { implicit dao: PureharmRowDAO[IO] =>
    for {
      _      <- dao.insert(row1)
      exists <- dao.exists(row1.id)
    } yield assert(exists)
  }

  test("insert + delete") { implicit dao: PureharmRowDAO[IO] =>
    for {
      _       <- dao.insert(row1)
      _       <- dao.delete(row1.id)
      deleted <- dao.find(row1.id)
      _ = assert(deleted.isEmpty)
      _ <- withClue("... insert again after delete") {
        for {
          _ <- dao.insert(row1)
          r <- dao.find(row1.id)
        } yield assert(r == Option(row1))
      }
    } yield succeed
  }

  test("insert + update (defined opt) + retrieve") { implicit dao: PureharmRowDAO[IO] =>
    for {
      _ <- dao.insert(row1)
      newRowWithSome = row1.copy(
        byte        = PhantomByte(111.toByte),
        int         = PhantomInt(42),
        long        = PhantomLong(6.toLong),
        bigDecimal  = PhantomBigDecimal(BigDecimal("328572")),
        string      = PhantomString("updated_string"),
        jsonbCol    = PureharmJSONCol(79, "new_json_col"),
        optionalCol = Option(PhantomString("new opt_value")),
      )
      _ <- dao.update(newRowWithSome)
      fetchedRow <- dao.retrieve(row1.id)
    } yield assert(newRowWithSome === fetchedRow)
  }

  test("insert + update (nulled opt) + retrieve") { implicit dao: PureharmRowDAO[IO] =>
    for {
      _ <- dao.insert(row1)
      newRowWithNone = row1.copy(
        byte        = PhantomByte(56.toByte),
        int         = PhantomInt(13),
        long        = PhantomLong(8.toLong),
        bigDecimal  = PhantomBigDecimal(BigDecimal("124325671")),
        string      = PhantomString("second_updated_string"),
        jsonbCol    = PureharmJSONCol(45, "newest_json_col"),
        optionalCol = Option.empty,
      )
      _ <- dao.update(newRowWithNone)
      fetchedRow <- dao.retrieve(row1.id)
    } yield assert(newRowWithNone === fetchedRow)
  }

  test("insertMany") { implicit dao: PureharmRowDAO[IO] =>
    for {
      _  <- dao.insertMany(List(row1, row2))
      r1 <- dao.find(row1.id)
      r2 <- dao.find(row2.id)
    } yield {
      assert(r1.nonEmpty, "row1")
      assert(r2.nonEmpty, "row2")
    }
  }

  test("insertMany — duplicate key") { implicit dao: PureharmRowDAO[IO] =>
    for {
      att <- dao.insertMany(List(row1, row1)).attempt
    } yield att match {
      case Left(err: DBBatchInsertFailedAnomaly) =>
        println(err.getLocalizedMessage)
        succeed
      case Left(err) => fail(s"invalid error type, got: ${err.getClass.getCanonicalName}")
      case Right(v)  => fail(s"should have failed, but got: $v")
    }
  }

  test("insertMany — deleteMany") { implicit dao: PureharmRowDAO[IO] =>
    for {
      _  <- dao.insertMany(List(row1, row2))
      _  <- dao.deleteMany(List(row1.id, row2.id))
      r1 <- dao.find(row1.id)
      r2 <- dao.find(row2.id)
    } yield {
      assert(r1.isEmpty, "r1 should have been deleted")
      assert(r2.isEmpty, "r2 should have been deleted")
    }
  }

  test("insert — existsAtLeastOne 1") { implicit dao: PureharmRowDAO[IO] =>
    for {
      _      <- dao.insertMany(List(row1, row2))
      exists <- dao.existsAtLeastOne(List(row1.id))
    } yield assert(exists)
  }

  test("insert — existsAtLeastOne 2") { implicit dao: PureharmRowDAO[IO] =>
    for {
      _      <- dao.insertMany(List(row2))
      exists <- dao.existsAtLeastOne(List(row1.id, row2.id))
    } yield assert(exists)
  }

  test("insert — existsAll — true ALL") { implicit dao: PureharmRowDAO[IO] =>
    for {
      _         <- dao.insertMany(List(row1, row2))
      existsAll <- dao.existAll(List(row1.id, row2.id))
    } yield assert(existsAll)
  }

  test("insert — existsAll — true SUBSET") { implicit dao: PureharmRowDAO[IO] =>
    for {
      _         <- dao.insertMany(List(row1, row2))
      existsAll <- dao.existAll(List(row1.id))
    } yield assert(existsAll)
  }

  test("insert — existsAll — FALSE") { implicit dao: PureharmRowDAO[IO] =>
    for {
      _         <- dao.insertMany(List(row1, row2))
      existsAll <- dao.existAll(List(row1.id, row2.id, PhantomPK("lskdfj")))
    } yield assert(!existsAll)
  }
}

private[test] object DoobieDAOAlgebraPureharmRowsTest {

  /**
    * All these values come from this file:
    * db/docker-pureharm-postgresql-test.sh
    *
    */
  private val dbConfig = DBConnectionConfig(
    host     = DBHost("localhost:20010"),
    dbName   = DatabaseName("pureharm_test"),
    username = DBUsername("pureharmony"),
    password = DBPassword("pureharmony"),
  )

  import doobie.util.ExecutionContexts

  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContexts.synchronous)

  //FIXME: add syntax on companion object that requires a DBConnectionConfig object + proper types
  def transactorResource[F[_]: Async: ContextShift]: Resource[F, Transactor[F]] = {
    val trans =
      Transactor
        .fromDriverManager[F](
          "org.postgresql.Driver", // driver classname
          dbConfig.jdbcURL:  String,
          dbConfig.username: String,
          dbConfig.password: String,
          Blocker.liftExecutionContext(ExecutionContexts.synchronous),
        )
        .pure[Resource[F, *]]
        .widen[Transactor[F]]

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
