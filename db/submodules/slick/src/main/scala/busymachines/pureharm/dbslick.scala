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
package busymachines.pureharm

import busymachines.pureharm.phdbslick.definitions

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 09 May 2019
  *
  */
object dbslick extends definitions.PureharmDBSlickTypeDefinitions {
  final type PureharmDBSlickTypeDefinitions = definitions.PureharmDBSlickTypeDefinitions
  final type PureharmDBSlickImplicits       = definitions.PureharmDBSlickImplicits
  final type SlickQueryAlgebraDefinitions   = phdbslick.SlickQueryAlgebraDefinitions
  final type SlickQueryAlgebraTypes         = definitions.SlickQueryAlgebraTypes

  final object implicits extends definitions.PureharmDBSlickImplicits
}
