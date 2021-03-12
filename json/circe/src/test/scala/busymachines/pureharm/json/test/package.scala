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
package busymachines.pureharm.json

import busymachines.pureharm.anomaly.InvalidInputAnomaly
import busymachines.pureharm.phantom._
import busymachines.pureharm.effects.implicits._

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 12 Jun 2019
  */
package object test {

  @scala.annotation.nowarn
  object Weight extends PhantomType[Int]
  type Weight = Weight.Type

  @scala.annotation.nowarn
  object SafeWeight extends SafePhantomType[Throwable, Int] {

    /** @return
      *   - Right — of the original (or transformed) value to be tagged
      *   - Left — of the failure type you want
      */
    override def check(value: Int): Either[Throwable, Int] = if (value > 0) {
      Either.right(value)
    }
    else {
      Either.left(InvalidInputAnomaly("Weight should be larger than 0"))
    }
  }
  type SafeWeight = SafeWeight.Type

  @scala.annotation.nowarn
  object Weights extends PhantomType[List[Int]]
  type Weights = Weights.Type

  @scala.annotation.nowarn
  object WeigthsSet extends PhantomType[Set[Int]]
  type WeigthsSet = WeigthsSet.Type

  @scala.annotation.nowarn
  object MelonDuo extends PhantomType[(Int, String)]
  type MelonDuo = MelonDuo.Type

  @scala.annotation.nowarn
  object MelonTrio extends PhantomType[(Int, String, List[Int])]
  type MelonTrio = MelonTrio.Type

}
