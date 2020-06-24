/**
  * Copyright (c) 2017-2019 BusyMachines
  *
  * See company homepage at: https://www.busymachines.com/
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
package busymachines.pureharm.testkit

import busymachines.pureharm.effects._

/**
  * Usually overriding the "executionContext" should be enough
  */
trait PureharmTestRuntime {

  private lazy val defaultExecutionContext: ExecutionContext = UnsafePools.cached("phtest-ec")
  private lazy val defaultContextShift:     ContextShift[IO] = IO.contextShift(executionContext)
  private lazy val defaultTimer:            Timer[IO]        = IO.timer(executionContext)

  private lazy val defaultBlockingShifter: BlockingShifter[IO] =
    BlockingShifter.fromExecutionContext(UnsafePools.cached("phtest-blocker"))

  implicit def executionContext: ExecutionContext    = defaultExecutionContext
  implicit def contextShift:     ContextShift[IO]    = defaultContextShift
  implicit def timer:            Timer[IO]           = defaultTimer
  implicit def blockingShifter:  BlockingShifter[IO] = defaultBlockingShifter
  implicit final def blocker:    Blocker             = blockingShifter.blocker

}
