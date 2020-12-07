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

import java.util.concurrent._

import busymachines.pureharm.effects.pools.ExecutionContextCT
import cats.effect._

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 15 Jun 2019
  */
private[pureharm] object PoolCached {

  def cached[F[_]: Sync](threadPrefixName: String, daemons: Boolean = false): Resource[F, ExecutionContextCT] = {
    val alloc = Sync[F].delay(unsafeExecutorService(threadPrefixName, daemons))
    val free  = (es: ExecutorService) => Sync[F].delay(es.shutdown())
    Resource.make(alloc)(free).map(es => ExecutionContextCT(Util.exitOnFatal(es)))
  }

  def unsafeCached(threadPrefixName: String, daemons: Boolean = false): ExecutionContextCT =
    ExecutionContextCT(Util.exitOnFatal(unsafeExecutorService(threadPrefixName, daemons)))

  private def unsafeExecutorService(threadPrefixName: String, daemons: Boolean): ExecutorService = {
    val prefix = s"$threadPrefixName-$Cached"
    Executors.newCachedThreadPool(Util.namedThreadPoolFactory(prefix, daemons))
  }

  private val Cached = "cached"
}
