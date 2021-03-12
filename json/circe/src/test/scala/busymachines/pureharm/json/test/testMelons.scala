/** Copyright (c) 2017-2019 BusyMachines
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
package busymachines.pureharm.json.test

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 11 Jun 2019
  */
private[test] case class AnarchistMelon(
  noGods:       Boolean,
  noMasters:    Boolean,
  noSuperTypes: Boolean,
)

sealed private[test] trait Melon {
  def weight: Int
}

private[test] case class WinterMelon(
  fuzzy:  Boolean,
  weight: Int,
) extends Melon

private[test] case class WaterMelon(
  seeds:  Boolean,
  weight: Int,
) extends Melon

private[test] case class PhantomMelon(
  weight:        Weight,
  safeWeight:    SafeWeight,
  refinedWeight: RefinedWeight,
  weights:       Weights,
  weightsSet:    WeigthsSet,
  duo:           MelonDuo,
  trio:          MelonTrio,
) extends Melon

private[test] case object SmallMelon extends Melon {
  override val weight: Int = 0
}

sealed private[test] trait Taste

private[test] case object SweetTaste extends Taste

//I ran out of ideas, ok? I'll think of better test data.
private[test] case object SourTaste extends Taste

sealed private[test] trait TastyMelon extends Melon {
  def tastes: Seq[Taste]
}

private[test] case class SquareMelon(
  weight: Int,
  tastes: Seq[Taste],
) extends TastyMelon
