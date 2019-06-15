/*
 * Code copied and adapted from cats-effect project:
 * https://github.com/typelevel/cats-effect/blob/03a555117b2037f29c2e4404921ece2bcccff58e/core/jvm/src/main/scala/cats/effect/internals/PoolUtils.scala
 *
 * Copyright (c) 2017-2019 The Typelevel Cats-effect Project Developers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package busymachines.pureharm.effects.pools

import cats.effect._
import java.util.concurrent._

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 15 Jun 2019
  */
private[pools] object PoolFixed {

  def fixed[F[_]: Sync](
    threadNamePrefix: String,
    maxThreads:       Int,
    daemons:          Boolean,
  ): Resource[F, ExecutionContextFT] = {
    val alloc = Sync[F].delay(unsafeExecutorService(threadNamePrefix, maxThreads, daemons))
    val free: ExecutorService => F[Unit] = (es: ExecutorService) => Sync[F].delay(es.shutdown())
    Resource.make(alloc)(free).map(es => ExecutionContextFT(Util.exitOnFatal(es)))
  }

  /**
    * Prefer [[fixed]], unless you know what you are doing.
    * The behavior the the Execution context itself is the same
    * for both, but the former is actually safer to use :)
    */
  def unsafeFixed(
    threadNamePrefix: String,
    maxThreads:       Int,
    daemons:          Boolean,
  ): ExecutionContextFT = {
    ExecutionContextFT(Util.exitOnFatal(unsafeExecutorService(threadNamePrefix, maxThreads, daemons)))
  }

  private def unsafeExecutorService(threadNamePrefix: String, maxThreads: Int, daemons: Boolean): ExecutorService = {
    val bound  = math.max(1, maxThreads)
    val prefix = s"$threadNamePrefix-$maxThreads"
    Executors.newFixedThreadPool(bound, Util.namedThreadPoolFactory(prefix, daemons))
  }
}
