package busymachines.pureharm

import org.scalatest.FunSpec

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 09 May 2019
  *
  */
final class SafePhantomTypeSpec extends FunSpec {
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
  private object SafeSpooked extends SafePhantomType[String, String] {
    override def check(value: String): Either[String, String] =
      if (value.isEmpty) Left[String, String](FailureString) else Right[String, String](value)
  }

  private type SafeSpooked = SafeSpooked.Type

  private[pureharm] object OtherSafeSpooked extends SafePhantomType[String, String] {
    override def check(value: String): Either[String, String] = ???
  }
  private[pureharm] type OtherSafeSpooked = OtherSafeSpooked.Type
}
