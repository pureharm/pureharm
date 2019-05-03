package busymachines.pureharm.effects_impl

/**
  *
  * Pseudo companion object for [[busymachines.pureharm.effects.BracketAttempt]]
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
  * @since 03 May 2019
  *
  */
object BracketAttempt {
  def apply[F[_]](implicit i: cats.effect.Bracket[F, Throwable]): cats.effect.Bracket[F, Throwable] = i
}
