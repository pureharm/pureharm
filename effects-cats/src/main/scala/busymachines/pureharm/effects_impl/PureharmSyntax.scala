package busymachines.pureharm.effects_impl

import busymachines.pureharm.effects

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 May 2019
  *
  */
object PureharmSyntax {

  trait Implicits {

    implicit def pureharmFOptionOps[F[_], A](foa: F[Option[A]]): FOptionOps[F, A] = new FOptionOps[F, A](foa)
    implicit def pureharmPureOptionOps[A](oa:     Option[A]):    PureOptionOps[A] = new PureOptionOps[A](oa)

    import effects.Attempt
    implicit def pureharmFAttemptOps[F[_], A](foa: F[Attempt[A]]): FAttemptOps[F, A] = new FAttemptOps[F, A](foa)
    implicit def pureharmPureAttemptOps[A](aa:     Attempt[A]):    PureAttemptOps[A] = new PureAttemptOps[A](aa)

    implicit def pureharmFBooleanOps[F[_]](fb: F[Boolean]): FBooleanOps[F] = new FBooleanOps[F](fb)
    implicit def pureharmPureBooleanOps(b:     Boolean):    PureBooleanOps = new PureBooleanOps(b)

    implicit def anyFOps[F[_], A](fa: F[A]): AnyFOps[F, A] = new AnyFOps[F, A](fa)
  }

  //--------------------------- OPTION ---------------------------

  final class FOptionOps[F[_], A](val foa: F[Option[A]]) extends AnyVal {
    import cats.implicits._

    def flatten(ifNone: => Throwable)(implicit F: cats.MonadError[F, Throwable]): F[A] =
      foa.flatMap(_.liftTo[F](ifNone))

    def ifSomeRaise(ifSome: A => Throwable)(implicit F: cats.MonadError[F, Throwable]): F[Unit] =
      foa.flatMap {
        case None    => F.unit
        case Some(v) => F.raiseError(ifSome(v))
      }

    def ifSomeRaise(ifSome: => Throwable)(implicit F: cats.MonadError[F, Throwable]): F[Unit] =
      foa.flatMap {
        case None    => F.unit
        case Some(_) => F.raiseError(ifSome)
      }

    def ifNoneRun(fu: F[_])(implicit F: cats.Monad[F]): F[Unit] = foa.flatMap {
      case None    => fu.void
      case Some(_) => F.unit
    }

    def ifSomeRun(fu: F[_])(implicit F: cats.Monad[F]): F[Unit] = foa.flatMap {
      case None    => F.unit
      case Some(_) => fu.void
    }

    def ifSomeRun(fuf: A => F[_])(implicit F: cats.Monad[F]): F[Unit] = foa.flatMap {
      case None    => F.unit
      case Some(a) => fuf(a).void
    }
  }

  final class PureOptionOps[A](val oa: Option[A]) extends AnyVal {
    import cats.implicits._

    def onError[F[_]](fu: F[_])(implicit F: cats.Monad[F]): F[Unit] = this.ifNoneRun(fu)

    def ifSomeRaise[F[_]](ifSome: A => Throwable)(implicit F: cats.MonadError[F, Throwable]): F[Unit] =
      oa match {
        case None    => F.unit
        case Some(v) => F.raiseError(ifSome(v))
      }

    def ifSomeRaise[F[_]](ifSome: => Throwable)(implicit F: cats.MonadError[F, Throwable]): F[Unit] =
      oa match {
        case None    => F.unit
        case Some(_) => F.raiseError(ifSome)
      }

    def ifNoneRun[F[_]](fu: F[_])(implicit F: cats.Monad[F]): F[Unit] = oa match {
      case None    => fu.void
      case Some(_) => F.unit
    }

    def ifSomeRun[F[_]](fu: F[_])(implicit F: cats.Monad[F]): F[Unit] = oa match {
      case None    => F.unit
      case Some(_) => fu.void
    }

    def ifSomeRun[F[_]](fuf: A => F[_])(implicit F: cats.Monad[F]): F[Unit] = oa match {
      case None    => F.unit
      case Some(a) => fuf(a).void
    }
  }

  //--------------------------- ATTEMPT ---------------------------

  final class FAttemptOps[F[_], A](val faa: F[effects.Attempt[A]]) {
    import cats.implicits._

    @scala.deprecated(
      "Use '.rethrow' from cats.MonadError — you should be able to just replace it right now with no additional effort or imports",
      "0.0.2",
    )
    def flatten(implicit F: cats.MonadError[F, Throwable]): F[A] =
      faa.flatMap(_.liftTo[F])
  }

  final class PureAttemptOps[A](val fa: effects.Attempt[A]) {
    import cats.implicits._

    /**
      * @return
      *  The original failure, or value, if the given effect also
      *  fails that failure is ignored
      */
    def onError[F[_]](fu: F[_])(implicit F: cats.MonadError[F, Throwable]): F[A] = fa match {
      case Left(thr) => fu.attempt.flatMap(_ => F.raiseError[A](thr))
      case Right(v)  => F.pure(v)
    }
  }

  //--------------------------- BOOLEAN ---------------------------

  final class FBooleanOps[F[_]](val fb: F[Boolean]) extends AnyVal {
    import cats.implicits._

    def ifFalseRaise(ifFalse: => Throwable)(implicit F: cats.MonadError[F, Throwable]): F[Unit] =
      fb.ifM(ifTrue = F.unit, ifFalse = F.raiseError(ifFalse))

    def ifTrueRaise(ifFalse: => Throwable)(implicit F: cats.MonadError[F, Throwable]): F[Unit] =
      fb.ifM(ifTrue = F.raiseError(ifFalse), ifFalse = F.unit)

    def ifFalseRun(fu: F[_])(implicit F: cats.Monad[F]): F[Unit] =
      fb.ifM(ifTrue = F.unit, ifFalse = fu.void)

    def ifTrueRun(fu: F[_])(implicit F: cats.Monad[F]): F[Unit] =
      fb.ifM(ifTrue = fu.void, ifFalse = F.unit)
  }

  final class PureBooleanOps(val b: Boolean) extends AnyVal {
    import cats.implicits._

    def ifFalseRaise[F[_]](ifFalse: => Throwable)(implicit F: cats.MonadError[F, Throwable]): F[Unit] =
      if (b) F.unit else F.raiseError(ifFalse)

    def ifTrueRaise[F[_]](ifFalse: => Throwable)(implicit F: cats.MonadError[F, Throwable]): F[Unit] =
      if (b) F.raiseError(ifFalse) else F.unit

    def ifFalseRun[F[_]](fu: F[_])(implicit F: cats.Monad[F]): F[Unit] =
      if (b) F.unit else fu.void

    def ifTrueRun[F[_]](fu: F[_])(implicit F: cats.Monad[F]): F[Unit] =
      if (b) fu.void else F.unit
  }

  //--------------------------- ANY F --------------------------
  final class AnyFOps[F[_], A](val fa: F[A]) extends AnyVal {
    import cats.implicits._
    import cats.effect.Sync

    /**
      * We want to be able to run arbitrary effects of the same type
      * if a Sync fails.
      */
    def onError(fu: F[_])(implicit F: Sync[F]): F[A] = fa.onError {
      case _ => fu.void
    }
  }
}
