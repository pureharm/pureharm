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
final class PoolsResourceTest extends AnyFunSuite {

  private val scalaTestTP = "ScalaTest"
  private val mainTP      = "ph-main"
  private val dbBlocTP    = "ph-db-block"
  private val dbConnTP    = "ph-db-conn"
  private val httpTP      = "ph-http"
  private val blockingTP  = "ph-blocking"
  private val singleTP    = "ph-single"

  implicit val (contextShift: ContextShift[IO], timer: Timer[IO]) = IORuntime.defaultMainRuntime(mainTP).value

  test("thread pool allocation and proper thread naming") {
    import PHTestPools._

    val io: IO[String] = testPools[IO].use { res =>
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

      p(s"Acquired resource") *> r <* p(s"Releasing resource")
    }

    withClue(s"after resource is de-allocated we should be on $mainTP") {
      (io >>= compareThreadName(mainTP)).unsafeRunSync()
    }

  }

  private def testPools[F[_]: Sync: ContextShift]: Resource[F, PHTestPools[F]] =
    for {
      nrOfCPUs <- Pools.availableCPUs[F]
      _        <- println(s"starting test w/ #of CPUs: $nrOfCPUs").pure[Resource[F, *]]

      dbBlockingCT    <- Pools.cached[F](dbBlocTP)
      dbConnectFT     <- Pools.fixed[F](dbConnTP, nrOfCPUs * 2)
      httpFT          <- Pools.fixed[F](httpTP, nrOfCPUs)
      blockingCT      <- Pools.cached[F](blockingTP)
      singleST        <- Pools.singleThreaded[F](singleTP)
      blockingShifter <- BlockingShifter.fromExecutionContext[F](blockingCT).pure[Resource[F, *]]
    } yield new PHTestPools[F](
      dbBlocking      = dbBlockingCT,
      dbConnection    = dbConnectFT,
      httpEC          = httpFT,
      single          = singleST,
      blockingShifter = blockingShifter,
    )

}
