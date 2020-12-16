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
package busymachines.pureharm.effects.pools

import busymachines.pureharm.internals.effects.pools.{PoolCached, PoolFixed, Util}
import cats.effect._

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 15 Jun 2019
  */
object Pools {

  def availableCPUs[F[_]: Sync]: Resource[F, Int] = Resource.liftF(Sync[F].delay(Util.unsafeAvailableCPUs))

  /** Cached pools should be used for blocking i/o. Without very
    * stringent back-pressure and 100% certainty that you never
    * overload with blocking i/o you will almost certainly
    * freeze your application into oblivion by doing blocking i/o
    * on a fixed thread pool.
    *
    * @param threadNamePrefix
    *   prefixes this name to the ThreadID. This is the name
    *   that usually shows up in the logs. It also prefixes
    *   "cached" to the names.
    * @param daemons
    *   whether or not the threads in the pool should be daemons or not.
    *   see [[java.lang.Thread#setDaemon(boolean)]] for the meaning
    *   for daemon threads.
    * @return
    */
  def cached[F[_]: Sync](
    threadNamePrefix: String = "cached",
    daemons:          Boolean = false,
  ): Resource[F, ExecutionContextCT] =
    PoolCached.cached(threadNamePrefix, daemons)

  /** The difference between this one and [[ExecutionContextMainFT]] is that
    * this one allows to us to have a fixed thread pool with 1 thread.
    *
    * N.B.: places where it is advisable to have a fixed thread pool:
    * - the pool that handles incoming HTTP requests (thus providing
    * some back-pressure by slowing down client requests)
    *
    * - the pool that handles the connections to your DB, thus
    * back-pressuring your DB server.
    *
    * As a side-note, you probably want your HTTP pool to be
    * smaller than your DB pool (since you might be doing multiple
    * DB calls per request).
    *
    * @param threadNamePrefix
    * prefixes this name to the ThreadID. This is the name
    * that usually shows up in the logs. It also prefixes
    * ``-ft${maxThreads}`` after the given string.
    * @param maxThreads
    * The maximum number of threads in the pool. Always defaults to 1 thread,
    * if you accidentally give it a value < 1.
    * @param daemons
    * whether or not the threads in the pool should be daemons or not.
    * see [[java.lang.Thread#setDaemon(boolean)]] for the meaning
    * for daemon threads.
    * @return
    *   A fixed thread pool
    */
  def fixed[F[_]: Sync](
    threadNamePrefix: String = "fixed",
    maxThreads:       Int,
    daemons:          Boolean = false,
  ): Resource[F, ExecutionContextFT] =
    PoolFixed.fixed(threadNamePrefix, maxThreads, daemons)

  /** A simple thread pool with one single thread. Be careful how you use it.
    */
  def singleThreaded[F[_]: Sync](
    threadNamePrefix: String = "single-thread",
    daemons:          Boolean = false,
  ): Resource[F, ExecutionContextST] =
    PoolFixed.fixed(threadNamePrefix, 1, daemons).map(ExecutionContextST.apply)

}
