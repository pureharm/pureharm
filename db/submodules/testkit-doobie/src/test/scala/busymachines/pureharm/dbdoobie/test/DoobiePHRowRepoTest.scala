/**
  * Copyright (c) 2019 BusyMachines
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
package busymachines.pureharm.dbdoobie.test

import busymachines.pureharm.db._
import busymachines.pureharm.db.testdata._
import busymachines.pureharm.db.testkit._
import busymachines.pureharm.dbdoobie._
import busymachines.pureharm.dbdoobie.test.DoobiePHRowRepo.DoobieDoobiePHRowTable
import busymachines.pureharm.dbdoobie.testkit._
import busymachines.pureharm.effects._
import busymachines.pureharm.testkit._
import org.scalatest._

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 25 Jun 2020
  */
final class DoobiePHRowRepoTest extends PHRowRepoTest[Transactor[IO]] with ParallelTestExecution {
  override type ResourceType = DoobiePHRowRepo[IO]

  override def setup: DBTestSetup[Transactor[IO]] = DoobiePHRowRepoTest

  override def resource(meta: MetaData, trans: Transactor[IO]): Resource[IO, DoobiePHRowRepo[IO]] =
    Resource.pure[IO, DoobiePHRowRepo[IO]](DoobiePHRowRepo(trans))

  test("insert row1 + row2 (w/ same unique_string) -> conflict") { implicit repo =>
    for {
      _       <- repo.insert(data.row1)
      attempt <-
        repo
          .insert(data.row2.copy(uniqueString = data.row1.uniqueString))
          .attempt
      failure = interceptFailure[DBUniqueConstraintViolationAnomaly](attempt)
    } yield {
      assert(failure.column == DoobieDoobiePHRowTable.unique_string, "column name")
      assert(failure.value == data.row1.uniqueString, "column name")
    }
  }

  test("insert row1 + row2 (w/ same unique_int) -> conflict") { implicit repo =>
    for {
      _       <- repo.insert(data.row1)
      attempt <-
        repo
          .insert(data.row2.copy(uniqueInt = data.row1.uniqueInt))
          .attempt
      failure = interceptFailure[DBUniqueConstraintViolationAnomaly](attempt)
    } yield {
      assert(failure.column == DoobieDoobiePHRowTable.unique_int, "column name")
      assert(failure.value == data.row1.uniqueInt.toString, "column name")
    }
  }

  test("insert row1 + row2 (w/ same unique_json) -> conflict") { implicit repo =>
    import DoobieDoobiePHRowTable.phJSONColJsonCodec
    import busymachines.pureharm.json.implicits._
    for {
      _       <- repo.insert(data.row1)
      attempt <-
        repo
          .insert(data.row2.copy(uniqueJSON = data.row1.uniqueJSON))
          .attempt
      failure = interceptFailure[DBUniqueConstraintViolationAnomaly](attempt)
    } yield {
      assert(failure.column == DoobieDoobiePHRowTable.unique_json, "column name")
      assertSuccess(failure.value.decodeAs[PHJSONCol])(data.row1.uniqueJSON)
    }
  }
}

object DoobiePHRowRepoTest extends DoobieDBTestSetup {

  override def dbConfig(meta: TestData)(implicit logger: TestLogger): DBConnectionConfig =
    PHRTestDBConfig.dbConfig.withSchemaFromClassAndTest(prefix = "doobie", meta = meta)

}
