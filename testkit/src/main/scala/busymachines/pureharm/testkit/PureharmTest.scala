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
import org.scalatest.funsuite.AnyFunSuite
import org.scalactic.source
import busymachines.pureharm.effects._
import org.scalatest.exceptions.{TestCanceledException, TestFailedException, TestPendingException}

/**
  *
  * This is an experimental base class,
  * at some point it should be moved to a testkit module
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 24 Jun 2020
  *
  */
abstract class PureharmTest extends AnyFunSuite with Assertions with PureharmTestRuntime {
  import io.chrisdavenport.log4cats._
  final type MetaData = TestData

  private val report: SelfAwareStructuredLogger[IO] =
    slf4j.Slf4jLogger.getLoggerFromName[IO](s"${getClass.getCanonicalName}.report")

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
      _        <- report.info(mdc)(s"STARTING")
      (d, att) <- testFun.timedAttempt(TimeUnit.MILLISECONDS)
      ass      <- att match {
        case Left(e: TestPendingException) =>
          report.info(mdc.++(MDCKeys(Pending, d)))("FINISHED") *> IO.raiseError[Assertion](e)

        case Left(e: TestFailedException) =>
          report.info(mdc.++(MDCKeys(Exceptional(e), d)))("FINISHED") *> IO.raiseError[Assertion](e)

        case Left(e: TestCanceledException) =>
          report.info(mdc.++(MDCKeys(Exceptional(e), d)))("FINISHED") *> IO.raiseError[Assertion](e)

        case Left(e) =>
          report.warn(mdc.++(MDCKeys(Exceptional(e), d)))(
            "TERMINATED — fail tests with assertions, not by throwing random"
          ) *> IO.raiseError[Assertion](e)

        case Right(ass) =>
          report.info(mdc.++(MDCKeys(Succeeded, d)))("FINISHED") *> IO.pure[Assertion](ass)

      }
    } yield ass

    super.test(testName, testTags: _*)(iotest.unsafeRunSync())
  }
}
