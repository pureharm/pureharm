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
import busymachines.pureharm.db._
import busymachines.pureharm.db.testdata._
import busymachines.pureharm.db.testkit._
import busymachines.pureharm.dbslick._
import busymachines.pureharm.dbslick.testkit._
import busymachines.pureharm.testkit._
import org.scalatest._

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
final class SlickPHRRepoTest extends PHRTestRepoTest[Transactor[IO]] with ParallelTestExecution {

  override type FixtureParam = SlickPHRTestRepo[IO]

  override def setup: RepoTestSetup[Transactor[IO]] = SlickPHRRepoTest

  override def fixture(meta: MetaData, trans: Transactor[IO]): Resource[IO, SlickPHRTestRepo[IO]] =
    Resource.pure[IO, SlickPHRTestRepo[IO]] {
      implicit val t:  Transactor[IO] = trans
      implicit val ec: ConnectionIOEC = ConnectionIOEC(runtime.executionContextCT)
      SlickPHRTestRepo[IO]
    }
}

private[test] object SlickPHRRepoTest extends SlickRepoTestSetup(testdb.jdbcProfileAPI) {

  override def dbConfig(meta: TestData)(implicit logger: TestLogger): DBConnectionConfig =
    PHRTestDBConfig.dbConfig.copy(
      schema = PHRTestDBConfig.schemaName(s"slick_${meta.pos.get.lineNumber}")
    )

}