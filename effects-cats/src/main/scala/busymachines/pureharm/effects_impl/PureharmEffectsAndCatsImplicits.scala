package busymachines.pureharm.effects_impl

/**
  *
  * Mix this trait into your own effects package to get all cats, cats-effect, and pureharm syntax in one
  * import!
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 06 May 2019
  *
  */
trait PureharmEffectsAndCatsImplicits extends PureharmEffectsSyntaxAll with CatsAliasesCore with CatsAliasesEffect
