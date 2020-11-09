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
package busymachines.pureharm.testkit

import java.util.concurrent.TimeUnit

import busymachines.pureharm.effects._
import busymachines.pureharm.testkit.util._
import org.scalactic.source
import org.scalatest._
import org.scalatest.exceptions._
import org.scalatest.funsuite.AnyFunSuite

/** This is an experimental base class,
  * at some point it should be moved to a testkit module
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 24 Jun 2020
  */
abstract class PureharmTest
  extends AnyFunSuite with PureharmAssertions with Assertions with PureharmTestRuntimeLazyConversions {
  final type MetaData = TestData

  private lazy val testLogger_ = TestLogger.fromClass(this.getClass)
  implicit def testLogger: TestLogger = testLogger_

  /** @see [[PureharmTestRuntimeLazyConversions]]
    *     for details as to why this is a def
    */
  implicit def runtime: PureharmTestRuntime = PureharmTestRuntime

  import busymachines.pureharm.effects.implicits._

  protected def test(
    testName: String,
    testTags: Tag*
  )(
    testFun:  IO[Assertion]
  )(implicit
    position: source.Position
  ): Unit = {

    val mdc = MDCKeys(testName, position)
    val iotest: IO[Assertion] = for {
      _        <- testLogger.info(mdc)(s"STARTING")
      (d, att) <- testFun.timedAttempt(TimeUnit.MILLISECONDS)
      ass      <- att match {
        case Left(e: TestPendingException) =>
          testLogger.info(mdc.++(MDCKeys(Pending, d)))("FINISHED") *> IO.raiseError[Assertion](e)

        case Left(e: TestFailedException) =>
          testLogger.info(mdc.++(MDCKeys(Exceptional(e), d)))("FINISHED") *> IO.raiseError[Assertion](e)

        case Left(e: TestCanceledException) =>
          testLogger.info(mdc.++(MDCKeys(Exceptional(e), d)))("FINISHED") *> IO.raiseError[Assertion](e)

        case Left(e) =>
          testLogger.warn(mdc.++(MDCKeys(Exceptional(e), d)))(
            "TERMINATED — fail tests with assertions, not by throwing random"
          ) *> IO.raiseError[Assertion](e)

        case Right(ass) =>
          testLogger.info(mdc.++(MDCKeys(Succeeded, d)))("FINISHED") *> IO.pure[Assertion](ass)

      }
    } yield ass

    super.test(testName, testTags: _*)(iotest.unsafeRunSync())
  }
}
