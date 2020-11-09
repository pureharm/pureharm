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

import java.util.concurrent.{ExecutorService, ThreadFactory}

import scala.concurrent.ExecutionContext
import scala.util.control.NonFatal

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 15 Jun 2019
  */
private[pureharm] object Util {

  def unsafeAvailableCPUs: Int = Runtime.getRuntime.availableProcessors()

  private[pools] def exitOnFatal(ec: ExecutorService): ExecutionContext = new ExecutionContext {
    private val underlying: ExecutionContext = ExecutionContext.fromExecutorService(ec)

    override def execute(r: Runnable): Unit = {
      underlying.execute(new Runnable {
        def run(): Unit = {
          try {
            r.run()
          }
          catch {
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
      underlying.reportFailure(t)
  }

  /** @param prefix
    *   A thread factory where the name + daemon status is prefixed to the thread ID.
    * @return
    */
  private[pools] def namedThreadPoolFactory(prefix: String, daemonThreads: Boolean): ThreadFactory = new ThreadFactory {

    override def newThread(r: Runnable): Thread = {
      val thread = new Thread(r)
      if (daemonThreads) {
        thread.setName(s"$prefix-$Daemon-${thread.getId}")
      }
      else {
        thread.setName(s"$prefix-${thread.getId}")
      }
      thread.setDaemon(daemonThreads)
      thread
    }
  }

  private val Daemon = "daemon"
}
