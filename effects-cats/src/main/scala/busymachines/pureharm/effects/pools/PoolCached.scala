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
import scala.concurrent._
import java.util.concurrent._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 15 Jun 2019
  *
  */
object PoolCached {

  /**
    * Cached pools should be used for blocking i/o. Without very
    * stringent back-pressure and 100% certainty that you never
    * overload with blocking i/o you will almost certainly
    * freeze your application into oblivion by doing blocking i/o
    * on a fixed thread pool.
    *
    * @param threadPrefixName
    *   prefixes this name to the ThreadID. This is the name
    *   that usually shows up in the logs. It also prefixes
    *   "cached" to the names.
    * @param daemons
    *   whether or not the threads in the pool should be daemons or not.
    *   see [[java.lang.Thread#setDaemon(boolean)]] for the meaning
    *   for daemon threads.
    * @return
    */
  def cached[F[_]: Sync](threadPrefixName: String, daemons: Boolean = false): Resource[F, ExecutionContextCT] = {
    val prefix = s"$threadPrefixName-$Cached"
    val alloc  = Sync[F].delay(unsafeExecutorService(prefix, daemons))
    val free   = (es: ExecutorService) => Sync[F].delay(es.shutdown())
    Resource.make(alloc)(free).map(es => ExecutionContextCT(Util.exitOnFatal(ExecutionContext.fromExecutorService(es))))
  }

  /**
    * Prefer [[cached]], unless you know what you are doing.
    * The behavior the the Execution context itself is the same
    * for both, but the former is actually safer to use :)
    */
  def unsafeCached(threadPrefixName: String, daemons: Boolean = false): ExecutionContextCT = {
    val prefix = s"$threadPrefixName-$Cached"
    ExecutionContextCT(Util.exitOnFatal(ExecutionContext.fromExecutorService(unsafeExecutorService(prefix, daemons))))
  }

  private def unsafeExecutorService(prefix: String, daemons: Boolean): ExecutorService = {
    java.util.concurrent.Executors.newCachedThreadPool(
      Util.namedThreadPoolFactory(prefix, daemons),
    )
  }

  private val Cached = "cached"
}
