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
package busymachines.pureharm.db.testkit

import busymachines.pureharm.effects._
import busymachines.pureharm.testkit.PureharmTestWithResource

abstract class DBTest[Trans] extends PureharmTestWithResource {
  def setup: DBTestSetup[Trans]

  def resource(meta: MetaData, trans: Trans): Resource[IO, ResourceType]

  override def resource(meta: MetaData): Resource[IO, ResourceType] =
    for {
      trans <- setup.transactor(meta)
      fix   <- resource(meta, trans)
    } yield fix
}
