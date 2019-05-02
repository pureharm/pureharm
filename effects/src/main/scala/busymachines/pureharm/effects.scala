package busymachines.pureharm

import busymachines.pureharm.{effects_impl => impl}

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 24 Apr 2019
  *
  */
object effects extends PureharmEffectsTypeDefinitions {

  /**
    * !!! N.B. !!!
    * NEVER, EVER wildcard import this, AND, cats.implicits, or anything from the cats packages.
    *
    * This object is meant to bring in everything that is in cats + some extra, without burdening
    * the user with two different imports.
    */
  object implicits extends PureharmEffectsSyntaxAll with impl.CatsAliases.Core with impl.CatsAliases.Effect
}
