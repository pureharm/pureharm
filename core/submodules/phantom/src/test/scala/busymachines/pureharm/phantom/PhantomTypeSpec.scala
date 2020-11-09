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
package busymachines.pureharm.phantom

import org.scalatest.funspec.AnyFunSpec

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 02 Apr 2019
  */
final class PhantomTypeSpec extends AnyFunSpec {
  import PhantomTypeSpec._

  describe("PhantomType") {
    it("should spook and despook the types") {
      val original:  String  = "EVERYTHING IS A SPOOK!"
      val spooked:   Spooked = Spooked.spook(original)
      val despooked: String  = Spooked.despook(spooked)

      assert(original === despooked)
    }

    it("should not allow assigning a base type to a PhantomType") {

      assertDoesNotCompile {
        """
          |val string: Spooked = "THIS IS A NON TAGGED STRING"
        """.stripMargin
      }
    }

    it("should not allow assigning different Phantoms of the same base type to each other") {

      assertDoesNotCompile {
        """
          |val spooked: Spooked = (??? : OtherSpooked)
        """.stripMargin
      }
    }
  }
}

private object PhantomTypeSpec {
  private object Spooked extends PhantomType[String]
  private type Spooked = Spooked.Type

  private[pureharm] object OtherSpooked extends PhantomType[String]
  private[pureharm] type OtherSpooked = OtherSpooked.Type
}
