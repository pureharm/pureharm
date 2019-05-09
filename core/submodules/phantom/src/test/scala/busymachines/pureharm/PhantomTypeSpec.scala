package busymachines.pureharm

import org.scalatest.FunSpec

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 02 Apr 2019
  *
  */
final class PhantomTypeSpec extends FunSpec {
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
