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
package busymachines.pureharm.effects.pools.test

import org.scalatest.funsuite.AnyFunSuite

import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._
import org.scalactic.source.Position
import org.scalatest.Assertion

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 15 Jun 2019
  *
  */
final class PoolsInitAndNamingTest extends AnyFunSuite {

  private val mainTP     = "ph-main"
  private val dbBlocTP   = "ph-db-block"
  private val dbConnTP   = "ph-db-conn"
  private val httpTP     = "ph-http"
  private val blockingTP = "ph-blocking"

  implicit val (contextShift: ContextShift[IO], timer: Timer[IO]) = IORuntime.defaultMainRuntime(mainTP)

  test("thread pool allocation and proper shifting") {

    val io: IO[String] = testPools[IO].use { res =>
      val r = for {
        _       <- contextShift.evalOn(res.httpEC)(tn >>= rtn(httpTP))
        _       <- contextShift.evalOn(res.dbBlocking)(tn >>= rtn(dbBlocTP))
        _       <- contextShift.evalOn(res.dbConnection)(tn >>= rtn(dbConnTP))
        _       <- res.blockingShifter.blockOn(tn >>= rtn(blockingTP))
        finalTN <- tn
      } yield finalTN

      p(s"Acquired resource") *> r <* p(s"Releasing resource")
    }

    withClue("after resource is de-allocated we should be on main thread") {
      (io >>= rtn(mainTP)).unsafeRunSync()
    }

  }

  private def testPools[F[_]: Sync: ContextShift]: Resource[F, TestPools[F]] =
    for {
      cpus <- Pools.availableCPUs[F]
      _    <- println(s"starting test w/ #of CPUs: $cpus").pure[Resource[F, ?]]

      dbBlockingCT <- Pools.cached[F](dbBlocTP)
      dbConnectFT  <- Pools.fixed[F](dbConnTP, cpus * 2)
      httpFT       <- Pools.fixed[F](httpTP, cpus)
      blocking     <- Pools.cached[F](blockingTP)

      blockingShifter <- BlockingShifter.fromExecutionContext[F](blocking).pure[Resource[F, ?]]
    } yield new TestPools[F](
      dbBlocking      = dbBlockingCT,
      dbConnection    = dbConnectFT,
      httpEC          = httpFT,
      blockingShifter = blockingShifter,
    )

  private def tn: IO[String] = IO(Thread.currentThread().getName)

  private def p(s: String): IO[Unit] = IO(println(s))

  private def rtn(loc: String)(tn: String)(implicit pos: Position): IO[Assertion] =
    p(s"should be on '$loc', and we are on: $tn") >> IO(assert(tn.contains(loc)))

  final private[PoolsInitAndNamingTest] class TestPools[F[_]](
    val dbBlocking:      ExecutionContextCT,
    val dbConnection:    ExecutionContextFT,
    val httpEC:          ExecutionContextFT,
    val blockingShifter: BlockingShifter[F],
  )
}
