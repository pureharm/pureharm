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
package busymachines.pureharm.effects.pools

import cats.effect.{ContextShift, IO, Timer}

/**
  *
  *
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 14 Jun 2019
  *
  */
object UnsafePools {

  def mainContextShiftPool(threadNamePrefix: String = "main-cpu-fixed"): ExecutionContextMainFT =
    PoolMainCPU.default(threadNamePrefix)

  def fixed(threadNamePrefix: String = "fixed", maxThreads: Int, daemons: Boolean = false): ExecutionContextFT =
    PoolFixed.unsafeFixed(threadNamePrefix, maxThreads, daemons)

  def cached(threadNamePrefix: String = "cached", daemons: Boolean = false): ExecutionContextCT =
    PoolCached.unsafeCached(threadNamePrefix, daemons)

  def singleThreaded(threadNamePrefix: String = "single-thread", daemons: Boolean = false): ExecutionContextFT =
    PoolFixed.unsafeFixed(threadNamePrefix, 1, daemons)

  def mainIOTimerFromEC(ec: ExecutionContextMainFT): Timer[IO] = IO.timer(ec)

  def mainIOContextShiftFromEC(ec: ExecutionContextMainFT): ContextShift[IO] = IO.contextShift(ec)

}
