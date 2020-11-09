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
package busymachines.pureharm.effects.pools.test

import org.scalatest.funsuite.AnyFunSuite

import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._

/** We usually run two things on a given thread pool,
  * so that one can manually inspect thread names in the console
  * to glimpse some behavior
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 15 Jun 2019
  */
final class UnsafePoolsTest extends AnyFunSuite {

  private val scalaTestTP = "ScalaTest"
  private val mainTP      = "ph-unsafe-main"
  private val dbBlocTP    = "ph-unsafe-db-block"
  private val dbConnTP    = "ph-unsafe-db-conn"
  private val httpTP      = "ph-unsafe-http"
  private val blockingTP  = "ph-unsafe-blocking"
  private val singleTP    = "ph-unsafe-single"

  implicit val (_, contextShift: ContextShift[IO], timer: Timer[IO]) = IORuntime.defaultMainRuntimeWithEC(mainTP).value

  test("thread pool allocation and proper thread naming") {
    import PHTestPools._

    val res = testPools[IO]

    val io: IO[String] = {
      val r = for {
        _ <- assertOnTP(scalaTestTP)
        _ <- contextShift.shift
        _ <- assertOnTP(mainTP)

        _ <- contextShift.evalOn(res.httpEC)(assertOnTP(httpTP))
        _ <- contextShift.evalOn(res.httpEC)(assertOnTP(httpTP))

        _ <- contextShift.evalOn(res.dbBlocking)(assertOnTP(dbBlocTP))
        _ <- contextShift.evalOn(res.dbBlocking)(assertOnTP(dbBlocTP))

        _ <- contextShift.evalOn(res.dbConnection)(assertOnTP(dbConnTP))
        _ <- contextShift.evalOn(res.dbConnection)(assertOnTP(dbConnTP))

        stn1 <- contextShift.evalOn(res.single)(assertOnTP(singleTP))
        stn2 <- contextShift.evalOn(res.single)(assertOnTP(singleTP))
        _    <- IO(assert(stn1 == stn2, "The thread names of on the single pool should always have the same name"))

        _ <- res.blockingShifter.blockOn(assertOnTP(blockingTP))
        _ <- res.blockingShifter.blockOn(assertOnTP(blockingTP))

        mt <- assertOnTP(mainTP)

      } yield mt

      p(s"Acquired resource, and starting test") *> r <* p(s"Ended test")
    }
    withClue(s"after resource is de-allocated we should be on $mainTP") {
      (io >>= compareThreadName(mainTP)).unsafeRunSync()
    }

  }

  private def testPools[F[_]: Sync: ContextShift]: PHTestPools[F] = {
    val nrOfCPUs        = UnsafePools.availableCPUs
    val dbBlockingCT    = UnsafePools.cached(dbBlocTP)
    val dbConnectFT     = UnsafePools.fixed(dbConnTP, nrOfCPUs * 2)
    val httpFT          = UnsafePools.fixed(httpTP, nrOfCPUs)
    val blockingCT      = UnsafePools.cached(blockingTP)
    val singleST        = UnsafePools.singleThreaded(singleTP)
    val blockingShifter = BlockingShifter.fromExecutionContext[F](blockingCT)
    new PHTestPools[F](
      dbBlocking      = dbBlockingCT,
      dbConnection    = dbConnectFT,
      httpEC          = httpFT,
      single          = singleST,
      blockingShifter = blockingShifter,
    )
  }

}
