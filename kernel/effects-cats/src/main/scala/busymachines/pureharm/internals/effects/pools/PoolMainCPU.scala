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
package busymachines.pureharm.internals.effects.pools

import busymachines.pureharm.effects.pools.ExecutionContextMainFT

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 15 Jun 2019
  */
private[pureharm] object PoolMainCPU {

  def default(threadNamePrefix: String): ExecutionContextMainFT =
    minTwoUnsafe(threadNamePrefix, Util.unsafeAvailableCPUs)

  def main(threadNamePrefix: String, maxThreads: Int): ExecutionContextMainFT =
    minTwoUnsafe(threadNamePrefix, maxThreads)

  private def minTwoUnsafe(threadNamePrefix: String, maxThreads: Int): ExecutionContextMainFT = {
    val bound = Math.max(2, maxThreads)
    ExecutionContextMainFT(PoolFixed.unsafeFixed(threadNamePrefix, bound, daemons = true))
  }

}
