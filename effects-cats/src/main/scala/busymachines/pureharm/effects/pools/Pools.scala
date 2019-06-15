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

import cats.effect._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 15 Jun 2019
  *
  */
object Pools {

  def cached[F[_]: Sync](
    threadNamePrefix: String  = "cached",
    daemons:          Boolean = false,
  ): Resource[F, ExecutionContextCT] =
    PoolCached.cached(threadNamePrefix, daemons)

  def fixed[F[_]: Sync](
    threadNamePrefix: String = "fixed",
    maxThreads:       Int,
    daemons:          Boolean = false,
  ): Resource[F, ExecutionContextFT] =
    PoolFixed.fixed(threadNamePrefix, maxThreads, daemons)

  def singleThreaded[F[_]: Sync](
    threadNamePrefix: String  = "single-thread",
    daemons:          Boolean = false,
  ): Resource[F, ExecutionContextFT] =
    PoolFixed.fixed(threadNamePrefix, 1, daemons)

}
