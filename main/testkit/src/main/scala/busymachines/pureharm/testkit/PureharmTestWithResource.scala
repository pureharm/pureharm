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
import org.scalatest.funsuite.FixtureAnyFunSuite

/** This is an experimental base class,
  * at some point it should be moved to a testkit module
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 13 Jun 2019
  */
abstract class PureharmTestWithResource
  extends FixtureAnyFunSuite with Assertions with PureharmAssertions with PureharmTestRuntimeLazyConversions {
  final override type FixtureParam = ResourceType
  final type MetaData              = TestData

  /** @see [[PureharmTestRuntimeLazyConversions]]
    *     for details as to why this is a def
    */
  implicit def runtime: PureharmTestRuntime = PureharmTestRuntime

  private lazy val testLogger_ = TestLogger.fromClass(this.getClass)
  implicit def testLogger: TestLogger = testLogger_

  import busymachines.pureharm.effects.implicits._

  type ResourceType
  /** Instead of the "before and after shit" simply init, and close
    * everything in this Resource...
    *
    * @param meta
    *  Use this information to create table names or something
    */
  def resource(meta: MetaData): Resource[IO, ResourceType]

  protected def test(
    testName: String,
    testTags: Tag*
  )(
    testFun:  ResourceType => IO[Assertion]
  )(implicit
    position: source.Position
  ): Unit =
    super.test(testName, testTags: _*)(fp => testFun(fp).unsafeRunSync())

  final override protected def withFixture(test: OneArgTest): Outcome = {
    val mdc: Map[String, String] = MDCKeys(test)

    def ftest(fix: ResourceType): IO[Outcome] =
      for {
        _        <- testLogger.info(mdc)(s"INITIALIZED")
        (d, out) <- IO.delay(test(fix)).timedAttempt(TimeUnit.MILLISECONDS)
        outcome  <- out.liftTo[IO]
        _        <- testLogger.info(mdc.++(MDCKeys(outcome, d)))(s"FINISHED")
      } yield outcome

    val fout: IO[Outcome] = for {
      _   <- testLogger.info(mdc)(s"ACQUIRING FIXTURE")
      out <- resource(test)
        .onError { case e => testLogger.warn(mdc, e)("INIT — FAILED").to[Resource[IO, *]] }
        .use(ftest)
    } yield out

    fout.unsafeRunSync()
  }
}
