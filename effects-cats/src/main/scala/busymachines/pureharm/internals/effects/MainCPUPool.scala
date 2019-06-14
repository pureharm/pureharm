/*
 * Code copied from cats-effect, and adapted to take a prefix
 * string parameter for thread names naming
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
package busymachines.pureharm.internals.effects

import java.util.concurrent.{Executors, ThreadFactory}

import scala.concurrent.ExecutionContext
import scala.util.control.NonFatal

/**
  * This is a reasonable default to back up your application's
  * ContextShift. Make sure that you NEVER do blocking I/O on this
  * thread pool. NEVER! — it's not that hard as long as you respect
  * referential transparency, and are careful with using 3rd party
  * libraries (especially Java ones).
  */
object MainCPUPool {

  /**
    *
    * Instantiates a fixed thread pool with the maximum threads
    * being equal to the number of available processors available
    * to the JVM (N.B., this number can be less than what your
    * hardware offers)
    *
    * @param threadNamePrefix
    *   prefixes this name to the ThreadID. This is the name
    *   that usually shows up in the logs.
    * @return
    */
  def default(threadNamePrefix: String): ExecutionContext = {
    cpuPool(threadNamePrefix, Runtime.getRuntime().availableProcessors())
  }

  /**
    *
    * @param threadNamePrefix
    *   prefixes this name to the ThreadID. This is the name
    *   that usually shows up in the logs.
    * @param maxThreads
    *   The minimum value for this value is always 2. We simply do not allow
    *   you to instantiate it with less than that, to quote the implementation
    *   from cats:
    *   "lower-bound of 2 to prevent pathological deadlocks on virtual machines"
    *   This method also simply ignores any value below 2 that you provide, and defaults
    *   to 2 in that case.
    * @return
    *  This is a slightly impure method as it will blow up in your face if maxThreads < 2,
    *  but it's the only place we accept such an affront.
    */
  def cpuPool(threadNamePrefix: String, maxThreads: Int): ExecutionContext = {
    // lower-bound of 2 to prevent pathological deadlocks on virtual machines
    val bound = math.max(2, maxThreads)
    val executor = Executors.newFixedThreadPool(
      bound,
      new ThreadFactory {
        override def newThread(r: Runnable): Thread = {
          val back = new Thread(r)
          back.setName(s"$threadNamePrefix-${back.getId}")
          back.setDaemon(true)
          back
        }
      },
    )

    exitOnFatal(ExecutionContext.fromExecutor(executor))
  }

  private def exitOnFatal(ec: ExecutionContext): ExecutionContext = new ExecutionContext {
    override def execute(r: Runnable): Unit = {
      ec.execute(new Runnable {
        def run(): Unit = {
          try {
            r.run()
          } catch {
            case NonFatal(t) =>
              reportFailure(t)

            case t: Throwable =>
              // under most circumstances, this will work even with fatal errors
              t.printStackTrace()
              System.exit(1)
          }
        }
      })
    }

    override def reportFailure(t: Throwable): Unit =
      ec.reportFailure(t)
  }
}
