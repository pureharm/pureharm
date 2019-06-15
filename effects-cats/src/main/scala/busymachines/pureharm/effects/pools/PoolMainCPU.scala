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

/**
  * This is a reasonable default to back up your application's
  * ContextShift. Make sure that you NEVER do blocking I/O on this
  * thread pool. NEVER! — it's not that hard as long as you respect
  * referential transparency, and are careful with using 3rd party
  * libraries (especially Java ones).
  *
  *
  * Additionally, we can't really instantiate one of these in a resource
  * "safe" manner, because we need it for the [[cats.effect.IOApp]].
  */
object PoolMainCPU {

  /**
    *
    * Instantiates a fixed thread pool with the maximum threads
    * being equal to the number of available processors available
    * to the JVM (N.B., this number can be less than what your
    * hardware offers), and a minimum of two threads in order to,
    * quoting cats-effect:
    *
    * "lower-bound of 2 to prevent pathological deadlocks on virtual machines"
    *
    * See: https://github.com/typelevel/cats-effect/pull/547
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
  def default(threadNamePrefix: String): ExecutionContextMainFT = {
    minTwoUnsafe(threadNamePrefix, Runtime.getRuntime().availableProcessors())
  }

  /**
    * Like [[default]], but with any number of threads between [2..n].
    */
  def main(threadNamePrefix: String, maxThreads: Int): ExecutionContextMainFT = {
    minTwoUnsafe(threadNamePrefix, maxThreads)
  }

  private def minTwoUnsafe(threadNamePrefix: String, maxThreads: Int): ExecutionContextMainFT = {
    val bound = Math.max(2, maxThreads)
    ExecutionContextMainFT(PoolFixed.unsafeFixed(threadNamePrefix, bound, daemons = true))
  }

}
