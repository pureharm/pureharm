package busymachines.pureharm.db.testkit

import busymachines.pureharm.db._
import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._
import busymachines.pureharm.testkit._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 25 Jun 2020
  *
  */
abstract class PureharmDAOTest[E, PK, Trans](implicit show: Show[PK]) extends FixturePureharmTest {
  override type FixtureParam <: DAOAlgebra[IO, E, PK]

  def data: PureharmDAOTestData[E, PK]

  def setup: PureharmDAOTestSetup[Trans]

  def fixture(meta: MetaData, trans: Trans): Resource[IO, FixtureParam]

  protected def dataSanityCheck: Resource[IO, Unit] =
    IO.delay {
        assume(data.pk1 != data.pk2, "Incorrect test data. primary keys have to be different for the two rows")
        assume(data.nonExistentPK != data.pk1, "Incorrect test data. nonExistentPK has to be different from PK1")
        assume(data.nonExistentPK != data.pk2, "Incorrect test data. nonExistentPK has to be different from PK2")
      }
      .to[Resource[IO, *]]
      .void

  override def fixture(meta: MetaData): Resource[IO, FixtureParam] =
    for {
      _     <- dataSanityCheck
      trans <- setup.transactor(meta)
      fix   <- fixture(meta, trans)
    } yield fix

  test("find nonExistentPK -> none") { implicit dao: FixtureParam =>
    for {
      att <- dao.find(data.nonExistentPK)
    } yield assert(att.isEmpty)
  }

  test("retrieve nonExistentPK —> failed") { implicit dao: FixtureParam =>
    for {
      att <- dao.retrieve(data.nonExistentPK).attempt
    } yield assertFailure[DBEntryNotFoundAnomaly](att)
  }

  test("exists in empty DB -> false") { implicit dao: FixtureParam =>
    for {
      exists <- dao.exists(data.pk1)
    } yield assert(!exists)
  }

  test("insert row1 + find -> some") { implicit dao: FixtureParam =>
    for {
      _          <- dao.insert(data.row1)
      fetchedRow <- dao.find(data.pk1).flattenOption(fail(s"PK=${data.pk1} row was not in database"))
    } yield assert(data.row1 === fetchedRow)
  }

  test("insert row1 + retrieve -> success") { implicit dao: FixtureParam =>
    for {
      _          <- dao.insert(data.row1)
      fetchedRow <- dao.retrieve(data.pk1)
    } yield assert(data.row1 === fetchedRow)
  }

  test("insert row1 + exists -> true") { implicit dao: FixtureParam =>
    for {
      _      <- dao.insert(data.row1)
      exists <- dao.exists(data.pk1)
    } yield assert(exists)
  }

  test("insert row1 + delete + find -> none") { implicit dao: FixtureParam =>
    for {
      _       <- dao.insert(data.row1)
      _       <- dao.delete(data.pk1)
      deleted <- dao.find(data.pk1)
      _ = assert(deleted.isEmpty)
      _ <- withClue("... insert again after delete") {
        for {
          _ <- dao.insert(data.row1)
          r <- dao.find(data.pk1)
        } yield assertSome(r)(data.row1)
      }
    } yield succeed
  }

  test("insert row + idempotent update") { implicit dao: FixtureParam =>
    for {
      _          <- dao.insert(data.row1)
      _          <- dao.update(data.row1)
      fetchedRow <- dao.retrieve(data.pk1)
    } yield assert(data.row1 === fetchedRow)
  }

  test("insert row1 + row1Update1") { implicit dao: FixtureParam =>
    for {
      _ <- dao.insert(data.row1)
      _        = assume(data.row1 != data.row2, "Incorrect test data. We need at least one row that's different from row1")
      toUpdate = data.row1Update1
      _          <- dao.update(toUpdate)
      fetchedRow <- dao.retrieve(data.pk1)
    } yield assert(toUpdate === fetchedRow)
  }

  if (data.isUniqueUpdate2) {
    test("insert row1 + row1Update2") { implicit dao: FixtureParam =>
      for {
        _ <- dao.insert(data.row1)
        toUpdate = data.row1Update2
        _          <- dao.update(toUpdate)
        fetchedRow <- dao.retrieve(data.pk1)
      } yield assert(toUpdate === fetchedRow)
    }
  }

  if (data.isUniqueUpdate3) {
    test("insert row1 + row1Update3") { implicit dao: FixtureParam =>
      for {
        _ <- dao.insert(data.row1)
        toUpdate = data.row1Update3
        _          <- dao.update(toUpdate)
        fetchedRow <- dao.retrieve(data.pk1)
      } yield assert(toUpdate === fetchedRow)
    }
  }

  if (data.isUniqueUpdate4) {
    test("insert row1 + row1Update4") { implicit dao: FixtureParam =>
      for {
        _ <- dao.insert(data.row1)
        toUpdate = data.row1Update4
        _          <- dao.update(toUpdate)
        fetchedRow <- dao.retrieve(data.pk1)
      } yield assert(toUpdate === fetchedRow)
    }
  }

  test("insertMany row1, row2 + find") { implicit dao: FixtureParam =>
    for {
      _  <- dao.insertMany(List(data.row1, data.row2))
      r1 <- dao.find(data.pk1)
      r2 <- dao.find(data.pk2)
    } yield {
      assert(r1.nonEmpty, "data.row1")
      assert(r2.nonEmpty, "data.row2")
    }
  }

  test("insertMany ro1, row1 -> duplicate key on row1") { implicit dao: FixtureParam =>
    for {
      att <- dao.insertMany(List(data.row1, data.row1)).attempt
      _ = assertFailure[DBBatchInsertFailedAnomaly](att)
      row1 <- dao.find(data.pk1)
    } yield assert(row1.isEmpty, "... on batch insert one failure, means all fail, but row1 was still added")
  }

  test("insertMany row1, row2 — deleteMany row1, row2 -> no rows should exist") { implicit dao: FixtureParam =>
    for {
      _  <- dao.insertMany(List(data.row1, data.row2))
      _  <- dao.deleteMany(List(data.pk1, data.pk2))
      r1 <- dao.find(data.pk1)
      r2 <- dao.find(data.pk2)
    } yield {
      assert(r1.isEmpty, "r1 should have been deleted")
      assert(r2.isEmpty, "r2 should have been deleted")
    }
  }

  test("insertMany row1, row2 — deleteMany row1 -> row2 should exist") { implicit dao: FixtureParam =>
    for {
      _  <- dao.insertMany(List(data.row1, data.row2))
      _  <- dao.deleteMany(List(data.pk1))
      r1 <- dao.find(data.pk1)
      r2 <- dao.find(data.pk2)
    } yield {
      assert(r1.isEmpty, "r1 should have been deleted")
      assert(r2.nonEmpty, "r2 should NOT have been deleted")
    }
  }

  test("insert row1, row2 — existsAtLeastOne row1, row2 -> true") { implicit dao: FixtureParam =>
    for {
      _      <- dao.insertMany(List(data.row1, data.row2))
      exists <- dao.existsAtLeastOne(List(data.pk1))
    } yield assert(exists)
  }

  test("insert row1 — existsAtLeastOne row1, row2 -> true") { implicit dao: FixtureParam =>
    for {
      _      <- dao.insertMany(List(data.row2))
      exists <- dao.existsAtLeastOne(List(data.pk1, data.pk2))
    } yield assert(exists)
  }

  test("insert row1, row2 — existsAll row1, row2 -> true") { implicit dao: FixtureParam =>
    for {
      _         <- dao.insertMany(List(data.row1, data.row2))
      existsAll <- dao.existAll(List(data.pk1, data.pk2))
    } yield assert(existsAll)
  }

  test("insert row1, row2 — existsAll row1 -> true") { implicit dao: FixtureParam =>
    for {
      _         <- dao.insertMany(List(data.row1, data.row2))
      existsAll <- dao.existAll(List(data.pk1))
    } yield assert(existsAll)
  }

  test("insert row1, row2 — existsAll row1, row2, nonExistent -> false") { implicit dao: FixtureParam =>
    for {
      _         <- dao.insertMany(List(data.row1, data.row2))
      existsAll <- dao.existAll(List(data.pk1, data.pk2, data.nonExistentPK))
    } yield assert(!existsAll)
  }
}
