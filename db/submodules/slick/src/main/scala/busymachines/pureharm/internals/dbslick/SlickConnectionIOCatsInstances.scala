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
package busymachines.pureharm.internals.dbslick

import busymachines.pureharm.effects.MonadError
import busymachines.pureharm.dbslick._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 04 Apr 2019
  *
  */
trait SlickConnectionIOCatsInstances {

  implicit final def connectionIOInstance(implicit ec: ConnectionIOEC): MonadError[ConnectionIO, Throwable] =
    new ConnectionIOMonadError
}
