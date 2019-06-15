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
import scala.concurrent._
import java.util.concurrent._

/**
  *
  * The difference between this one and [[PoolMainCPU]] is that
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
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 15 Jun 2019
  *
  */
object PoolFixed {

  /**
    *
    * @param threadNamePrefix
    *   prefixes this name to the ThreadID. This is the name
    *   that usually shows up in the logs. It also prefixes
    *   the total number of threads in the thread pool in the
    *   name.
    * @param maxThreads
    *   The maximum number of threads in the pool. Always defaults to 1 thread,
    *   if you accidentally give it a value < 1.
    * @param daemons
    *   whether or not the threads in the pool should be daemons or not.
    *   see [[java.lang.Thread#setDaemon(boolean)]] for the meaning
    *   for daemon threads.
    * @return
    *   A fixed thread pool
    */
  def fixed[F[_]: Sync](
    threadNamePrefix: String,
    maxThreads:       Int,
    daemons:          Boolean,
  ): Resource[F, ExecutionContextFT] = {
    val bound  = math.max(1, maxThreads)
    val prefix = s"$threadNamePrefix-$bound"

    val alloc = Sync[F].delay(unsafeExecutorService(prefix, bound, daemons))
    val free: ExecutorService => F[Unit] = (es: ExecutorService) => Sync[F].delay(es.shutdown())
    Resource.make(alloc)(free).map(es => ExecutionContextFT(Util.exitOnFatal(ExecutionContext.fromExecutorService(es))))
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
    val bound  = math.max(1, maxThreads)
    val prefix = s"$threadNamePrefix-$bound"
    ExecutionContextFT(
      Util.exitOnFatal(ExecutionContext.fromExecutorService(unsafeExecutorService(prefix, bound, daemons))),
    )
  }

  private def unsafeExecutorService(prefix: String, maxThreads: Int, daemons: Boolean): ExecutorService = {
    Executors.newFixedThreadPool(
      maxThreads,
      Util.namedThreadPoolFactory(prefix, daemons),
    )
  }
}
