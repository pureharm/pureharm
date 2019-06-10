package busymachines.pureharm.effects_impl

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jun 2019
  *
  */
object types {

  type Attempt[+R] = Either[Throwable, R]
  val Attempt: Either.type = Either

  type AttemptT[F[_], R] = cats.data.EitherT[F, Throwable, R]
  val AttemptT: cats.data.EitherT.type = cats.data.EitherT
  /**
    * Useful since we don't have partial kind application by default
    * Usage:
    * {{{
    *   def canFail[F[_]: ApplicativeAttempt, T](p1: T) : F[T] = ???
    * }}}
    */
  type ApplicativeAttempt[F[_]] = cats.ApplicativeError[F, Throwable]

  /**
    * Useful since we don't have partial kind application by default
    * Usage:
    * {{{
    *   def canFail[F[_]: MonadAttempt, T](p1: T) : F[T] = ???
    * }}}
    */
  type MonadAttempt[F[_]] = cats.MonadError[F, Throwable]

  type BracketAttempt[F[_]] = cats.effect.Bracket[F, Throwable]

}
