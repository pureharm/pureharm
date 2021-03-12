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
package busymachines.pureharm.config

import busymachines.pureharm.anomaly.InvalidInputAnomaly
import busymachines.pureharm.phantom._

import scala.concurrent.duration._

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 16 Jun 2019
  */
package object test {
  @scala.annotation.nowarn
  object PhantomInt            extends PhantomType[Int]
  @scala.annotation.nowarn
  object PhantomString         extends PhantomType[String]
  @scala.annotation.nowarn
  object PhantomBoolean        extends PhantomType[Boolean]
  @scala.annotation.nowarn
  object PhantomList           extends PhantomType[List[Int]]
  @scala.annotation.nowarn
  object PhantomSet            extends PhantomType[Set[String]]
  @scala.annotation.nowarn
  object PhantomFiniteDuration extends PhantomType[FiniteDuration]
  @scala.annotation.nowarn
  object PhantomDuration       extends PhantomType[Duration]

  type PhantomInt            = PhantomInt.Type
  type PhantomString         = PhantomString.Type
  type PhantomBoolean        = PhantomBoolean.Type
  type PhantomList           = PhantomList.Type
  type PhantomSet            = PhantomSet.Type
  type PhantomFiniteDuration = PhantomFiniteDuration.Type
  type PhantomDuration       = PhantomDuration.Type
  type SafePhantomInt        = SafePhantomInt.Type

  @scala.annotation.nowarn
  object SafePhantomInt extends SafePhantomType[Throwable, Int] {

    override def check(value: Int): Either[Throwable, Int] = {
      import busymachines.pureharm.effects.implicits._
      if (value > 0) Either.right(value)
      else Either.left(InvalidInputAnomaly(s"TEST_CASE_INVALID_SAFE_PHANTOM_ANOMALY"))
    }
  }
}
