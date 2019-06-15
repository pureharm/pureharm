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

import scala.concurrent._
import cats.effect._

/**
  * This is a reasonable default to back up your application's
  * ContextShift. Make sure that you NEVER do blocking I/O on this
  * thread pool. NEVER! — it's not that hard as long as you respect
  * referential transparency, and are careful with using 3rd party
  * libraries (especially Java ones).
  */
object PoolMainCPU {

  /**
    *
    * Instantiates a fixed thread pool with the maximum threads
    * being equal to the number of available processors available
    * to the JVM (N.B., this number can be less than what your
    * hardware offers)
    *
    * @param threadNamePrefix
    *   prefixes this name to the ThreadID. This is the name
    *   that usually shows up in the logs. It also prefixes
    *   the total number of threads in the thread pool in the
    *   name.
    * @return
    *   A fixed thread pool with at least two fixed threads,
    *   or the number of available processors.
    */
  def default[F[_]: Sync](threadNamePrefix: String): Resource[F, ExecutionContext] = {
    minTwoSafe(threadNamePrefix, Runtime.getRuntime().availableProcessors())
  }

  /**
    * Prefer [[default]], unless you know what you are doing.
    * The behavior the the Execution context itself is the same
    * for both, but the former is actually safer to use :)
    */
  def unsafeDefault(threadNamePrefix: String): ExecutionContext = {
    minTwo(threadNamePrefix, Runtime.getRuntime().availableProcessors())
  }

  private def minTwo(threadNamePrefix: String, maxThreads: Int): ExecutionContext = {
    // lower-bound of 2 to prevent pathological deadlocks on virtual machines
    val bound = Math.max(2, maxThreads)
    PoolFixed.unsafeFixed(threadNamePrefix, bound, daemons = true)
  }

  private def minTwoSafe[F[_]: Sync](threadNamePrefix: String, maxThreads: Int): Resource[F, ExecutionContext] = {
    // lower-bound of 2 to prevent pathological deadlocks on virtual machines
    val bound = Math.max(2, maxThreads)
    PoolFixed.fixed(threadNamePrefix, bound, daemons = true)
  }

}
