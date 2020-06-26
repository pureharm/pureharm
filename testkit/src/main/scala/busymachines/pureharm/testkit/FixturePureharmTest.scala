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
package busymachines.pureharm.testkit

import java.util.concurrent.TimeUnit

import org.scalatest._
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
abstract class FixturePureharmTest
  extends FixtureAnyFunSuite with Assertions with PureharmAssertions with PureharmTestRuntime {
  import io.chrisdavenport.log4cats._
  final type MetaData = TestData

  private lazy val testLogger_ = TestLogger.fromClass(this.getClass)
  implicit def testLogger: TestLogger = testLogger_

  import busymachines.pureharm.effects.implicits._
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
    testFun:  FixtureParam => IO[Assertion]
  )(implicit
    position: source.Position
  ): Unit =
    super.test(testName, testTags: _*)(fp => testFun(fp).unsafeRunSync())

  final override protected def withFixture(test: OneArgTest): Outcome = {
    val mdc: Map[String, String] = MDCKeys(test)

    def ftest(fix: FixtureParam): IO[Outcome] =
      for {
        _        <- testLogger.info(mdc)(s"INITIALIZED")
        (d, out) <- IO.delay(test(fix)).timedAttempt(TimeUnit.MILLISECONDS)
        outcome  <- out.liftTo[IO]
        _        <- testLogger.info(mdc.++(MDCKeys(outcome, d)))(s"FINISHED")
      } yield outcome

    val fout: IO[Outcome] = for {
      _   <- testLogger.info(mdc)(s"ACQUIRING FIXTURE")
      out <- fixture(test)
        .onError {
          case e => testLogger.warn(mdc, e)("INIT — FAILED").to[Resource[IO, *]]
        }
        .use(ftest)
    } yield out

    fout.unsafeRunSync()
  }
}
