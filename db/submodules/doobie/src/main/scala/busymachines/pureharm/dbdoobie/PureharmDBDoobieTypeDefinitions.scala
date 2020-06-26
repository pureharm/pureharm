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
package busymachines.pureharm.dbdoobie

import busymachines.pureharm.internals

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 24 Sep 2019
  *
  */
trait PureharmDBDoobieTypeDefinitions
  extends doobie.Aliases with doobie.free.Types with doobie.free.Modules with doobie.postgres.free.Types
  with doobie.postgres.free.Modules with doobie.postgres.hi.Modules {

  val ConnectionIO: doobie.implicits.AsyncConnectionIO.type = doobie.implicits.AsyncConnectionIO

  /**
    * Denotes the EC on which connections are managed,
    * backed up by a fixed thread pool with the number of threads
    * equal to the number of connections
    */
  val DoobieConnectionEC: internals.dbdoobie.DoobieConnectionEC.type = internals.dbdoobie.DoobieConnectionEC
  type DoobieConnectionEC = internals.dbdoobie.DoobieConnectionEC

  /**
    * Denotes the EC on which transactions(dbops) are managed,
    * backed up by a cached thread pool because blocking
    * i/o is executed on this one
    */
  val DoobieBlocker: internals.dbdoobie.DoobieBlocker.type = internals.dbdoobie.DoobieBlocker
  type DoobieBlocker = internals.dbdoobie.DoobieBlocker
}
