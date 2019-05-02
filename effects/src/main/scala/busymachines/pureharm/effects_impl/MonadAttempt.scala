package busymachines.pureharm.effects_impl

import busymachines.pureharm.effects

/**
  *
  * Pseudo companion object for [[busymachines.pureharm.effects.MonadAttempt]]
  * type, alias.
  *
  * The reason this is not implemented using the same pattern as in [[AttemptSyntax]]
  * is because we cannot overload the apply method from ``MonadError`` based
  * on type parameters. We'd need to be able to call an apply method with only the
  * effect value, which is not possible, so we simply duplicate whatever is in
  * [[cats.ApplicativeError]], luckily it only has only one method, unlike
  * [[scala.Either]] which has A LOT!
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 29 Apr 2019
  *
  */
object MonadAttempt {
  def apply[F[_]](implicit F: effects.MonadAttempt[F]): effects.MonadAttempt[F] = F
}
