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
  * @since 09 May 2019
  */
final class SafePhantomTypeSpec extends AnyFunSpec {
  import SafePhantomTypeSpec._

  describe("PhantomType") {
    it("right — should spook and despook the types") {
      val original: String                      = "EVERYTHING IS A SPOOK!"
      val spooked:  Either[String, SafeSpooked] = SafeSpooked.spook(original)

      assert(spooked === Right(original))

      val despooked: String = SafeSpooked.despook(spooked.getOrElse(fail("should be Right")))

      assert(original === despooked)
    }

    it("left — fail if check does not pass") {
      val original: String                      = ""
      val spooked:  Either[String, SafeSpooked] = SafeSpooked.spook(original)

      assert(spooked === Left(FailureString))
    }

    it("should not allow assigning a base type to a SafePhantomType") {

      assertDoesNotCompile {
        """
          |val string: Spooked = "THIS IS A NON TAGGED STRING"
        """.stripMargin
      }
    }

    it("should not allow assigning different Phantoms of the same base type to each other") {
      assertDoesNotCompile {
        """
          |val safeSpooked: SafeSpooked = (??? : OtherSafeSpooked)
        """.stripMargin
      }
    }
  }
}

private object SafePhantomTypeSpec {
  private val FailureString = "cannot be empty"

  @scala.annotation.nowarn
  private object SafeSpooked extends SafePhantomType[String, String] {

    override def check(value: String): Either[String, String] =
      if (value.isEmpty) Left[String, String](FailureString) else Right[String, String](value)
  }

  private type SafeSpooked = SafeSpooked.Type

  @scala.annotation.nowarn
  private[pureharm] object OtherSafeSpooked extends SafePhantomType[String, String] {
    override def check(value: String): Either[String, String] = ???
  }
  private[pureharm] type OtherSafeSpooked = OtherSafeSpooked.Type
}
