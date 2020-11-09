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
package busymachines.pureharm.db.testkit

import busymachines.pureharm.anomaly.InconsistentStateCatastrophe
import busymachines.pureharm.db._
import busymachines.pureharm.db.flyway.FlywayConfig
import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._
import busymachines.pureharm.testkit._
import busymachines.pureharm.testkit.util.{MDCKeys, PureharmTestRuntime}
import org.scalatest.TestData

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 25 Jun 2020
  */
trait DBTestSetup[DBTransactor] {
  final type RT = PureharmTestRuntime

  implicit class TestSetupClassName(config: DBConnectionConfig) {

    /** @see [[schemaName]]
      */
    def withSchemaFromClassAndTest(meta:   TestData): DBConnectionConfig =
      config.copy(schema = Option(schemaName(meta)))

    /** @see [[schemaName]]
      */
    def withSchemaFromClassAndTest(prefix: String, meta: TestData): DBConnectionConfig =
      config.copy(schema = Option(schemaName(prefix, meta)))
  }

  /** Should be overridden to create a connection config appropriate for the test
    *
    * To ensure unique schema names for test cases use the extension methods:
    * [[TestSetupClassName.withSchemaFromClassAndTest]]
    * or the explicit variants [[schemaName]]
    */
  def dbConfig(meta: TestData)(implicit logger: TestLogger): DBConnectionConfig

  //hack to remove unused param error
  def flywayConfig(meta: TestData): Option[FlywayConfig] = Option(meta) >> Option.empty

  protected def dbTransactorInstance(meta: TestData)(implicit rt: RT, logger: TestLogger): Resource[IO, DBTransactor]

  def transactor(meta: TestData)(implicit rt: RT, logger: TestLogger): Resource[IO, DBTransactor] =
    for {
      _ <- logger.info(MDCKeys(meta))("SETUP — init").to[Resource[IO, *]]
      schema = dbConfig(meta).schema.getOrElse("public")
      _       <-
        logger
          .info(MDCKeys(meta) ++ Map("schema" -> schema))(s"SETUP — schema name for test: $schema")
          .to[Resource[IO, *]]
      _       <- _cleanDB(meta)
      _       <- _initDB(meta)
      fixture <- dbTransactorInstance(meta)
    } yield fixture

  protected def _initDB(meta: TestData)(implicit rt: RT, logger: TestLogger): Resource[IO, Unit] =
    for {
      _    <- logger.info(MDCKeys(meta))("SETUP — preparing DB").to[Resource[IO, *]]
      migs <- flyway.Flyway.migrate[IO](dbConfig = dbConfig(meta), flywayConfig(meta)).to[Resource[IO, *]]
      _    <- (migs <= 0).ifTrueRaise[Resource[IO, *]](
        InconsistentStateCatastrophe(
          """
            |Number of migrations run is 0.
            |
            |Meaning that the migrations are not in the proper folder.
            |
            |Please make sure to move them to the appropriate location corresponding to
            |where your test is. This is a common mistake... in Intellij it doesn't matter
            |in which module the migrations are... but it matters for SBT
            |
            |That's why you probably didn't encounter this damn bug so soon.
            |""".stripMargin
        )
      )

      _ <- logger.info(MDCKeys(meta))("SETUP — done preparing DB").to[Resource[IO, *]]
    } yield ()

  protected def _cleanDB(meta: TestData)(implicit rt: RT, logger: TestLogger): Resource[IO, Unit] =
    for {
      _ <- logger.info(MDCKeys(meta))("SETUP — cleaning DB for a clean slate").to[Resource[IO, *]]
      _ <- flyway.Flyway.clean[IO](dbConfig(meta)).to[Resource[IO, *]]
      _ <- logger.info(MDCKeys(meta))("SETUP — done cleaning DB").to[Resource[IO, *]]
    } yield ()

  /** @return
    *   The schema name in the format of:
    *   ${getClass.SimpleName()_${testLineNumber Fallback to testName hash if line number not available}}
    */
  def schemaName(meta: TestData): SchemaName =
    truncateSchemaName(SchemaName(s"${schemaNameFromClassAndLineNumber(meta)}"))

  /** @return
    *   The schema name in the format of:
    *   $prefix_${getClass.SimpleName()_${testLineNumber Fallback to testName hash if line number not available}}
    */
  def schemaName(prefix: String, meta: TestData): SchemaName =
    truncateSchemaName(SchemaName(s"${prefix}_${schemaNameFromClassAndLineNumber(meta)}"))

  protected def truncateSchemaName(s: SchemaName): SchemaName = SchemaName(s.takeRight(63))

  protected def schemaNameFromClassAndLineNumber(meta: TestData): SchemaName =
    SchemaName(s"${schemaNameFromClass}_${lineNumberOrTestNameHash(meta)}")

  protected def schemaNameFromClass: String =
    getClass.getSimpleName.replace("$", "").toLowerCase

  /** When creating a schema we discriminate using the line number when defined,
    * otherwise using the hash of the test name, we print it out to the console,
    * so no worries, you can still identify the test easily.
    * @return
    */
  protected def lineNumberOrTestNameHash(meta: TestData): String =
    meta.pos.map(_.lineNumber.toString).getOrElse(s"${meta.name.hashCode.toString}")
}
