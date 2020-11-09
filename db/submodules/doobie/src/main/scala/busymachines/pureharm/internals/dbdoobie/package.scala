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
package busymachines.pureharm.internals

import busymachines.pureharm.effects._
import busymachines.pureharm.phantom._

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 26 Jun 2020
  */
package object dbdoobie {

  /** Denotes the EC on which connections are managed,
    * backed up by a fixed thread pool with the number of threads
    * equal to the number of connections
    */
  object DoobieConnectionEC extends PhantomType[ExecutionContext] {
    def safe(ec: ExecutionContextFT): this.Type = this.apply(ec)
  }

  type DoobieConnectionEC = DoobieConnectionEC.Type

  /** Denotes the EC on which transactions(dbops) are managed,
    * backed up by a cached thread pool because blocking
    * i/o is executed on this one
    */
  type DoobieBlocker = DoobieBlocker.Type

  object DoobieBlocker extends PhantomType[Blocker] {
    def safe(ec: ExecutionContextCT): this.Type = this(Blocker.liftExecutionContext(ec))
  }

}
