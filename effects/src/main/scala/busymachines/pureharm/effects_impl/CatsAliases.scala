package busymachines.pureharm.effects_impl

import cats.effect.syntax.AllCatsEffectSyntax
import cats.{instances, syntax}

/**
  *
  * !! WARNING !! these traits will have to be upgraded with each new cats
  * release. Simply check the implementation of cats.implicits and of
  * cats.effect.implicits and make sure that the mixed in traits are always
  * the same.
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 25 Apr 2019
  *
  */
private[pureharm] object CatsAliases {

  trait Core
      extends syntax.AllSyntax with syntax.AllSyntaxBinCompat0 with syntax.AllSyntaxBinCompat1
      with syntax.AllSyntaxBinCompat2 with syntax.AllSyntaxBinCompat3 with syntax.AllSyntaxBinCompat4
      with instances.AllInstances with instances.AllInstancesBinCompat0 with instances.AllInstancesBinCompat1
      with instances.AllInstancesBinCompat2 with instances.AllInstancesBinCompat3

  trait Effect extends AllCatsEffectSyntax
}
