package busymachines.pureharm.core

import org.specs2.mutable

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 02 Apr 2019
  *
  */
final class PhantomTypeSpec extends mutable.Specification {
  import PhantomTypeSpec._

  "PhantomType" >> {
    "should haunt and excorcise the types" >> {
      val original:  String  = "EVERYTHING IS A SPOOK!"
      val haunted:   Spooked = Spooked.spook(original)
      val exorcised: String  = Spooked.despook(haunted)

      original must_=== exorcised
    }
  }
}

private object PhantomTypeSpec {
  private object Spooked extends PhantomType[String]
  private type Spooked = Spooked.Type
}
