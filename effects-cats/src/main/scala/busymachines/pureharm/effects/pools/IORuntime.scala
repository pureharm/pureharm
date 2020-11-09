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

import cats.Later
import cats.effect.{ContextShift, IO, Timer}

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 15 Jun 2019
  */
object IORuntime {

  /** Useful to create all needed machinery to properly work with
    * the cats-effect runtime.
    *
    * You really should instantiate this once per app, therefore also
    * the [[Later]] return type, to properly hammer the point home.
    */
  def defaultMainRuntime(
    threadNamePrefix: String = "main-cpu-fixed"
  ): Later[(ContextShift[IO], Timer[IO])] = Later {
    val ec = UnsafePools.defaultMainExecutionContext(threadNamePrefix)
    (mainIOContextShift(ec), mainIOTimer(ec))
  }

  /** Use [[UnsafePools.defaultMainExecutionContext]], then pass it here, or use
    * [[defaultMainRuntime]] to instantiate all basic machinery.
    */
  def mainIOTimer(ec: ExecutionContextMainFT): Timer[IO] = IO.timer(ec)

  /** Use [[UnsafePools.defaultMainExecutionContext]], then pass it here, or use
    * [[defaultMainRuntime]] to instantiate all basic machinery.
    */
  def mainIOContextShift(ec: ExecutionContextMainFT): ContextShift[IO] = IO.contextShift(ec)

  /** Most likely you need only [[defaultMainRuntime]], because
    * there's little reason to expose the EC underlying your
    * [[ContextShift]] and [[Timer]].
    *
    * But in the cases you need that, this method behaves like
    * [[defaultMainRuntime]], but it also gives you the
    * underlying main thread pool
    */
  def defaultMainRuntimeWithEC(
    threadNamePrefix: String = "main-cpu-fixed"
  ): Later[(ExecutionContextMainFT, ContextShift[IO], Timer[IO])] = Later {
    val ec = UnsafePools.defaultMainExecutionContext(threadNamePrefix)
    (ec, mainIOContextShift(ec), mainIOTimer(ec))
  }
}
