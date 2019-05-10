/**
  * Copyright (c) 2019 BusyMachines
  *
  * See company homepage at: https://www.busymachines.com/
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package busymachines.pureharm.effects_impl

import busymachines.pureharm.effects._

import scala.collection.generic.CanBuildFrom

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

    implicit def pureharmFAttemptOps[F[_], A](foa: F[Attempt[A]]): FAttemptOps[F, A] = new FAttemptOps[F, A](foa)
    implicit def pureharmPureAttemptOps[A](aa:     Attempt[A]):    PureAttemptOps[A] = new PureAttemptOps[A](aa)

    implicit final def pureharmAttemptPseudoCompanionSyntax(companion: Either.type): AttemptPseudoCompanionSyntax =
      new AttemptPseudoCompanionSyntax(companion)

    implicit final def pureharmTryCompanionSyntax(companion: Try.type): TryCompanionSyntax =
      new TryCompanionSyntax(companion)

    implicit final def pureharmFBooleanOps[F[_]](fb: F[Boolean]): FBooleanOps[F] = new FBooleanOps[F](fb)
    implicit final def pureharmPureBooleanOps(b:     Boolean):    PureBooleanOps = new PureBooleanOps(b)

    implicit final def pureharmAnyFOps[F[_], A](fa: F[A]): AnyFOps[F, A] = new AnyFOps[F, A](fa)

    implicit final def pureharmFuturePseudoCompanionOps(c: Future.type): FuturePseudoCompanionOps =
      new FuturePseudoCompanionOps(c)

    implicit final def pureharmFutureReferenceEagerOps[A](f: Future[A]): FutureReferenceEagerOps[A] =
      new FutureReferenceEagerOps[A](f)

    implicit final def pureharmFutureReferenceDelayedOps[A](f: => Future[A]): FutureReferenceDelayedOps[A] =
      new FutureReferenceDelayedOps[A](f)
  }

  //--------------------------- OPTION ---------------------------

  final class FOptionOps[F[_], A] private[PureharmSyntax] (val foa: F[Option[A]]) extends AnyVal {
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

  final class PureOptionOps[A] private[PureharmSyntax] (val oa: Option[A]) extends AnyVal {
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

  final class FAttemptOps[F[_], A] private[PureharmSyntax] (val faa: F[Attempt[A]]) extends AnyVal {
    import cats.implicits._

    @scala.deprecated(
      "Use '.rethrow' from cats.MonadError — you should be able to just replace it right now with no additional effort or imports",
      "0.0.2",
    )
    def flatten(implicit F: cats.MonadError[F, Throwable]): F[A] =
      faa.flatMap(_.liftTo[F])
  }

  final class PureAttemptOps[A] private[PureharmSyntax] (val fa: Attempt[A]) extends AnyVal {
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

  private val singletonUnitAttempt: Attempt[Unit] = Right[Throwable, Unit](())

  /**
    * This helps mimick operations on the ``Attempt`` using
    * the standard ``Either`` companion, thus making all
    * those ops also available.
    *
    * See [[MonadAttempt]] and [[ApplicativeAttempt]] for further
    * details.
    */
  final class AttemptPseudoCompanionSyntax private[PureharmSyntax] (val companion: Either.type) extends AnyVal {
    def pure[A](a: A): Attempt[A] = Right[Throwable, A](a)

    def raiseError[A](t: Throwable): Attempt[A] = Left[Throwable, A](t)

    def unit: Attempt[Unit] = singletonUnitAttempt
  }

  //--------------------------- TRY ---------------------------

  private val singletonUnitTry: Try[Unit] = TrySuccess[Unit](())

  final class TryCompanionSyntax private[PureharmSyntax] (val companion: Try.type) extends AnyVal {
    def pure[A](a: A): Try[A] = TrySuccess(a)

    def raiseError[A](t: Throwable): Try[A] = TryFailure(t)

    def catchNonFatal[A](thunk: => A): Try[A] = companion.apply[A](thunk)

    def unit: Try[Unit] = singletonUnitTry
  }

  //--------------------------- BOOLEAN ---------------------------

  final class FBooleanOps[F[_]] private[PureharmSyntax] (val fb: F[Boolean]) extends AnyVal {
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

  final class PureBooleanOps private[PureharmSyntax] (val b: Boolean) extends AnyVal {
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

  final class AnyFOps[F[_], A] private[PureharmSyntax] (val fa: F[A]) extends AnyVal {
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

  //--------------------------- Future --------------------------

  /**
    * The scala standard library is extremely annoying because
    * various effects don't have similar syntax for essentially
    * the same operation.
    */
  final class FuturePseudoCompanionOps private[PureharmSyntax] (val companion: Future.type) extends AnyVal {
    def pure[A](a: A): Future[A] = companion.successful(a)

    def raiseError[A](t: Throwable): Future[A] = companion.failed(t)

    //=========================================================================
    //=============================== Traversals ==============================
    //=========================================================================

    /**
      *
      * Similar to [[scala.concurrent.Future.traverse]], but discards all content. i.e. used only
      * for the combined effects.
      *
      * @see [[scala.concurrent.Future.traverse]]
      */
    @inline def traverse_[A, B, M[X] <: TraversableOnce[X]](in: M[A])(fn: A => Future[B])(
      implicit
      cbf: CanBuildFrom[M[A], B, M[B]],
      ec:  ExecutionContext,
    ): Future[Unit] = FutureOps.traverse_(in)(fn)

    /**
      *
      * Similar to [[scala.concurrent.Future.sequence]], but discards all content. i.e. used only
      * for the combined effects.
      *
      * @see [[scala.concurrent.Future.sequence]]
      */
    @inline def sequence_[A, M[X] <: TraversableOnce[X]](in: M[Future[A]])(
      implicit
      cbf: CanBuildFrom[M[Future[A]], A, M[A]],
      ec:  ExecutionContext,
    ): Future[Unit] = FutureOps.sequence_(in)

    /**
      *
      * Syntactically inspired from [[Future.traverse]], but it differs semantically
      * insofar as this method does not attempt to run any futures in parallel. "M" stands
      * for "monadic", as opposed to "applicative" which is the foundation for the formal definition
      * of "traverse" (even though in Scala it is by accident-ish)
      *
      * For the vast majority of cases you should prefer this method over [[Future.sequence]]
      * and [[Future.traverse]], since even small collections can easily wind up queuing so many
      * [[Future]]s that you blow your execution context.
      *
      * Usage:
      * {{{
      *   import busymachines.effects.async._
      *   val patches: Seq[Patch] = //...
      *
      *   //this ensures that no two changes will be applied in parallel.
      *   val allPatches: Future[Seq[Patch]] = Future.serialize(patches){ patch: Patch =>
      *     Future {
      *       //apply patch
      *     }
      *   }
      *   //... and so on, and so on!
      * }}}
      *
      *
      */
    @inline def serialize[A, B, C[X] <: TraversableOnce[X]](col: C[A])(fn: A => Future[B])(
      implicit
      cbf: CanBuildFrom[C[A], B, C[B]],
      ec:  ExecutionContext,
    ): Future[C[B]] = FutureOps.serialize(col)(fn)

    /**
      * @see [[serialize]]
      *
      * Similar to [[serialize]], but discards all content. i.e. used only
      * for the combined effects.
      */
    @inline def serialize_[A, B, C[X] <: TraversableOnce[X]](col: C[A])(fn: A => Future[B])(
      implicit
      cbf: CanBuildFrom[C[A], B, C[B]],
      ec:  ExecutionContext,
    ): Future[Unit] = FutureOps.serialize_(col)(fn)
  }

  final class FutureReferenceEagerOps[A] private[PureharmSyntax] (val f: Future[A]) extends AnyVal {
    import scala.concurrent.duration._
    def unsafeRunSync(atMost: Duration = Duration.Inf): A = FutureOps.unsafeRunSync(f, atMost)
  }

  final class FutureReferenceDelayedOps[A] private[PureharmSyntax] (f: => Future[A]) {
    /**
      *
      * Suspend the side-effects of this [[Future]] into an [[F]] which can be converted from
      * and [[IO]]. This is the important operation when it comes to inter-op between
      * standard Scala and typelevel.
      *
      * Usage. N.B. that this only makes sense if the creation of the Future itself
      * is also suspended in the [[F]].
      * {{{
      * @inline def  writeToDB(v: Int, s: String): Future[Long] = ???
      *   //...
      *   val io = writeToDB(42, "string").suspendIn[IO]
      *   //no database writes happened yet, since the future did
      *   //not do its annoying running of side-effects immediately!
      *
      *   //when we want side-effects:
      *   io.unsafeGetSync()
      * }}}
      *
      * This is almost useless unless you are certain that ??? is a pure computation
      * {{{
      *   val f: Future[Int] = Future.apply(???)
      *   f.suspendIn[IO]
      * }}}
      *
      */
    def suspendIn[F[_]: LiftIO]: F[A] = IO.fromFuture(IO(f)).to[F]
  }

}
