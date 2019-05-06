package busymachines.pureharm

import busymachines.pureharm.effects_impl

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 24 Apr 2019
  *
  */
object effects extends effects_impl.PureharmEffectsTypeDefinitions {

  //mix these into your libraries for one import experience
  type PureharmEffectsSyntaxAll        = effects_impl.PureharmEffectsSyntaxAll
  type PureharmEffectsTypeDefinitions  = effects_impl.PureharmEffectsTypeDefinitions
  type PureharmEffectsAndCatsImplicits = effects_impl.PureharmEffectsAndCatsImplicits

  /**
    * !!! N.B. !!!
    * NEVER, EVER wildcard import this, AND, cats.implicits, or anything from the cats packages.
    *
    * This object is meant to bring in everything that is in cats + some extra, without burdening
    * the user with two different imports.
    */
  object implicits extends effects_impl.PureharmEffectsAndCatsImplicits
}
