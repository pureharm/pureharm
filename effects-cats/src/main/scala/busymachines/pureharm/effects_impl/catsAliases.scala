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
package busymachines.pureharm.effects_impl

import cats.effect.syntax.AllCatsEffectSyntax
import cats.{instances, syntax}

/**
  *
  * !! WARNING !! these traits will have to be upgraded with each new cats
  * release. Simply check the implementation of cats.implicits and of
  * cats.effect.implicits and make sure that the mixed in traits are always
  * the same.
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 25 Apr 2019
  *
  */

trait CatsAliasesCore
    extends syntax.AllSyntax with syntax.AllSyntaxBinCompat0 with syntax.AllSyntaxBinCompat1
    with syntax.AllSyntaxBinCompat2 with syntax.AllSyntaxBinCompat3 with syntax.AllSyntaxBinCompat4
    with instances.AllInstances with instances.AllInstancesBinCompat0 with instances.AllInstancesBinCompat1
    with instances.AllInstancesBinCompat2 with instances.AllInstancesBinCompat3

trait CatsAliasesEffect extends AllCatsEffectSyntax
