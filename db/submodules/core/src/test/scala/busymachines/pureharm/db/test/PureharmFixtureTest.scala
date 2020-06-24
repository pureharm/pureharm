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
package busymachines.pureharm.db.test

import java.util.concurrent.TimeUnit

import org.scalatest._
import org.scalatest.matchers.should.Matchers
import org.scalatest.funsuite.FixtureAnyFunSuite
import org.scalactic.source
import busymachines.pureharm.effects._

/**
  *
  * This is an experimental base class,
  * at some point it should be moved to a testkit module
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 13 Jun 2019
  *
  */
abstract class PureharmFixtureTest extends FixtureAnyFunSuite with Matchers {
  import io.chrisdavenport.log4cats._
  final type MetaData = TestData

  private val report: SelfAwareStructuredLogger[IO] =
    slf4j.Slf4jLogger.getLoggerFromName[IO](s"${getClass.getCanonicalName}.report")

  import busymachines.pureharm.effects.implicits._
  //for tests if fine if we just dump everything in global EC.
  //But for production this is an absolute nightmare and you should never do this
  implicit lazy val executionContext: ExecutionContext = ExecutionContext.global
  implicit lazy val contextShift:     ContextShift[IO] = IO.contextShift(executionContext)
  implicit lazy val timer:            Timer[IO]        = IO.timer(executionContext)

  /**
    * Instead of the "before and after shit" simply init, and close
    * everything in this Resource...
    *
    * @param meta
    *  Use this information to create table names or something
    */
  def fixture(meta: MetaData): Resource[IO, FixtureParam]

  protected def test(
    testName: String,
    testTags: Tag*
  )(
    testFun:  FixtureParam => IO[_]
  )(implicit
    position: source.Position
  ): Unit =
    super.test(testName, testTags: _*)(fp => testFun(fp).unsafeRunSync())

  final override protected def withFixture(test: OneArgTest): Outcome = {
    def ftest(fix: FixtureParam): IO[Outcome] =
      for {
        _        <- report.info(mdc(test))(s"INITIALIZED")
        (d, out) <- IO.delay(test(fix)).timedAttempt(TimeUnit.MILLISECONDS)
        outcome  <- out.liftTo[IO]
        _        <- report.info(
          mdc(test).++(
            Map(
              "outcome"  -> outcome.productPrefix,
              "duration" -> d.toString,
            )
          )
        )(s"FINISHED")
      } yield outcome

    val fout: IO[Outcome] = fixture(test)
      .onError {
        case e => Resource.liftF[IO, Unit](report.warn(Map("test" -> s"'${test.name}'"), e)("INIT — FAILED"))
      }
      .use(ftest)

    fout.unsafeRunSync()
  }

  private def mdc(test: TestData): Map[String, String] =
    test.pos match {
      case None      =>
        Map("test" -> s"'${test.name}'")
      case Some(pos) =>
        Map("test" -> s"'${test.name}'", "line" -> pos.lineNumber.toString)
    }

}
