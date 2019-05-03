package busymachines.pureharm.effects_impl

import busymachines.pureharm.effects

/**
  *
  * Pseudo companion object for [[busymachines.pureharm.effects.ApplicativeAttempt]]
  * type, alias.
  *
  * The reason this is not implemented using the same pattern as in [[AttemptSyntax]]
  * is because we cannot overload the apply method from ``ApplicativeError`` based
  * on type parameters. We'd need to be able to call an apply method with only the
  * effect value, which is not possible, so we simply duplicate whatever is in
  * [[cats.ApplicativeError]], luckily it only has like 3 methods, unlike
  * [[scala.Either]] which has A LOT!
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 29 Apr 2019
  *
  */
object ApplicativeAttempt {
  def apply[F[_]](implicit F: effects.ApplicativeAttempt[F]): effects.ApplicativeAttempt[F] = F

  //alias
  @inline def liftFromOption[F[_]] = cats.ApplicativeError.liftFromOption[F]
}
