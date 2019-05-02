package busymachines.pureharm.effects_impl

import busymachines.pureharm.effects._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 25 Apr 2019
  *
  */
object AttemptSyntax {

  private val singletonUnitValue: Attempt[Unit] = Right[Throwable, Unit](())

  trait Implicits {
    implicit final def pureharmAttemptPseudoCompanionSyntax(
      companion: Either.type,
    ): AttemptPseudoCompanionSyntax =
      new AttemptPseudoCompanionSyntax(companion)
  }

  final class AttemptPseudoCompanionSyntax private[AttemptSyntax] (
    val companion: Either.type,
  ) extends AnyVal {
    def pure[A](a: A): Attempt[A] = Right[Throwable, A](a)

    def raiseError[A](t: Throwable): Attempt[A] = Left[Throwable, A](t)

    def unit: Attempt[Unit] = singletonUnitValue
  }

}
