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
    it("should haunt and excorcise the types") {
      val original:  String  = "EVERYTHING IS A SPOOK!"
      val spooked:   Spooked = Spooked.spook(original)
      val despooked: String  = Spooked.despook(spooked)

      assert(original === despooked)
    }

    it("should not allow asigning a base type to a PhantomType") {

      assertDoesNotCompile {
        """
          |val string: Spooked = "THIS IS A NON TAGGED STRING"
        """.stripMargin
      }
    }
  }
}

private object PhantomTypeSpec {
  private object Spooked extends PhantomType[String]
  private type Spooked = Spooked.Type
}
