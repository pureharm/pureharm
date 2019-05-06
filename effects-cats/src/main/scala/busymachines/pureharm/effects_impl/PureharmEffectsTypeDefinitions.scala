package busymachines.pureharm.effects_impl

import busymachines.pureharm.effects_impl
import cats.{effect => ce}

import scala.{concurrent => sc}

/**
  *
  * Trait to mixin all sorts of type aliases for most commonly used
  * effects. This trait should be mixed in each of your projects
  * to gain "one import experience" for your effect needs.
  *
  * This brings in together all types from cats + cats-effect
  * and some standard scala types
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 24 Apr 2019
  *
  */
trait PureharmEffectsTypeDefinitions {

  type Functor[F[_]] = cats.Functor[F]
  val Functor: cats.Functor.type = cats.Functor

  type Applicative[F[_]] = cats.Applicative[F]
  val Applicative: cats.Applicative.type = cats.Applicative

  type Apply[F[_]] = cats.Apply[F]
  val Apply: cats.Apply.type = cats.Apply

  type FlatMap[F[_]] = cats.FlatMap[F]
  val FlatMap: cats.FlatMap.type = cats.FlatMap

  type CoflatMap[F[_]] = cats.CoflatMap[F]
  val CoflatMap: cats.CoflatMap.type = cats.CoflatMap

  type Monad[F[_]] = cats.Monad[F]
  val Monad: cats.Monad.type = cats.Monad

  type ApplicativeError[F[_], E] = cats.ApplicativeError[F, E]
  val ApplicativeError: cats.ApplicativeError.type = cats.ApplicativeError

  type MonadError[F[_], E] = cats.MonadError[F, E]
  val MonadError: cats.MonadError.type = cats.MonadError

  type Traverse[F[_]] = cats.Traverse[F]
  val Traverse: cats.Traverse.type = cats.Traverse

  type NonEmptyTraverse[F[_]] = cats.NonEmptyTraverse[F]
  val NonEmptyTraverse: cats.NonEmptyTraverse.type = cats.NonEmptyTraverse

  type UnorderedTraverse[F[_]] = cats.UnorderedTraverse[F]
  val UnorderedTraverse: cats.UnorderedTraverse.type = cats.UnorderedTraverse

  type TraverseFilter[F[_]] = cats.TraverseFilter[F]
  val TraverseFilter: cats.TraverseFilter.type = cats.TraverseFilter

  type Bitraverse[F[_, _]] = cats.Bitraverse[F]
  val Bitraverse: cats.Bitraverse.type = cats.Bitraverse

  type Semigroupal[F[_]] = cats.Semigroupal[F]
  val Semigroupal: cats.Semigroupal.type = cats.Semigroupal

  type Eq[A] = cats.Eq[A]
  val Eq: cats.Eq.type = cats.Eq

  type PartialOrder[A] = cats.PartialOrder[A]
  val PartialOrder: cats.PartialOrder.type = cats.PartialOrder

  type Comparison = cats.Comparison
  val Comparison: cats.Comparison.type = cats.Comparison

  type Order[A] = cats.Order[A]
  val Order: cats.Order.type = cats.Order

  type Hash[A] = cats.Hash[A]
  val Hash: cats.Hash.type = cats.Hash

  type Semigroup[A] = cats.Semigroup[A]
  val Semigroup: cats.Semigroup.type = cats.Semigroup

  type Monoid[A] = cats.Monoid[A]
  val Monoid: cats.Monoid.type = cats.Monoid

  type Group[A] = cats.Group[A]
  val Group: cats.Group.type = cats.Group

  //---------- monad transformers ----------------

  type EitherT[F[_], L, R] = cats.data.EitherT[F, L, R]
  val EitherT: cats.data.EitherT.type = cats.data.EitherT

  type OptionT[F[_], A] = cats.data.OptionT[F, A]
  val OptionT: cats.data.OptionT.type = cats.data.OptionT

  //---------- cats-data ----------------
  type NEList[+A] = cats.data.NonEmptyList[A]
  val NEList: cats.data.NonEmptyList.type = cats.data.NonEmptyList

  type NonEmptyList[+A] = cats.data.NonEmptyList[A]
  val NonEmptyList: cats.data.NonEmptyList.type = cats.data.NonEmptyList

  type NESet[A] = cats.data.NonEmptySet[A]
  val NESet: cats.data.NonEmptySet.type = cats.data.NonEmptySet

  type NonEmptySet[A] = cats.data.NonEmptySet[A]
  val NonEmptySet: cats.data.NonEmptySet.type = cats.data.NonEmptySet

  type NonEmptyMap[K, +A] = cats.data.NonEmptyMap[K, A]
  val NonEmptyMap: cats.data.NonEmptyMap.type = cats.data.NonEmptyMap

  type NEMap[K, +A] = cats.data.NonEmptyMap[K, A]
  val NEMap: cats.data.NonEmptyMap.type = cats.data.NonEmptyMap

  type Chain[+A] = cats.data.Chain[A]
  val Chain: cats.data.Chain.type = cats.data.Chain

  type NonEmptyChain[+A] = cats.data.NonEmptyChain[A]
  val NonEmptyChain: cats.data.NonEmptyChain.type = cats.data.NonEmptyChain

  //NE is much shorter than NonEmpty!
  type NEChain[+A] = cats.data.NonEmptyChain[A]
  val NEChain: cats.data.NonEmptyChain.type = cats.data.NonEmptyChain

  //---------- cats-misc ---------------------

  type Show[T] = cats.Show[T]
  val Show: cats.Show.type = cats.Show

  //----------- cats-effect types -----------
  type Sync[F[_]] = ce.Sync[F]
  val Sync: ce.Sync.type = ce.Sync

  type Async[F[_]] = ce.Async[F]
  val Async: ce.Async.type = ce.Async

  type Effect[F[_]] = ce.Effect[F]
  val Effect: ce.Effect.type = ce.Effect

  type Concurrent[F[_]] = ce.Concurrent[F]
  val Concurrent: ce.Concurrent.type = ce.Concurrent

  type ConcurrentEffect[F[_]] = ce.ConcurrentEffect[F]
  val ConcurrentEffect: ce.ConcurrentEffect.type = ce.ConcurrentEffect

  type Timer[F[_]] = ce.Timer[F]
  val Timer: ce.Timer.type = ce.Timer

  type ContextShift[F[_]] = ce.ContextShift[F]
  val contextShift: ce.ContextShift.type = ce.ContextShift

  type CancelToken[F[_]] = ce.CancelToken[F]
  type Fiber[F[_], A]    = ce.Fiber[F, A]
  val Fiber: ce.Fiber.type = ce.Fiber

  type SyncIO[+A] = ce.SyncIO[A]
  val SyncIO: ce.SyncIO.type = ce.SyncIO

  type IO[+A] = ce.IO[A]
  val IO: ce.IO.type = ce.IO

  type IOApp = ce.IOApp
  val IOApp: ce.IOApp.type = ce.IOApp

  type ExitCode = ce.ExitCode
  val ExitCode: ce.ExitCode.type = ce.ExitCode

  type Bracket[F[_], E] = ce.Bracket[F, E]
  val Bracket: ce.Bracket.type = ce.Bracket

  type Resource[F[_], A] = ce.Resource[F, A]
  val Resource: ce.Resource.type = ce.Resource

  type ExitCase[+E] = ce.ExitCase[E]
  val ExitCase: ce.ExitCase.type = ce.ExitCase

  //----------- cats-effect concurrent -----------

  type Semaphore[F[_]] = ce.concurrent.Semaphore[F]
  val Semaphore: ce.concurrent.Semaphore.type = ce.concurrent.Semaphore

  type Deferred[F[_], A] = ce.concurrent.Deferred[F, A]
  val Deferred: ce.concurrent.Deferred.type = ce.concurrent.Deferred

  type TryableDeferred[F[_], A] = ce.concurrent.TryableDeferred[F, A]
  //intentionally has same companion object as Deferred.
  val TryableDeferred: ce.concurrent.Deferred.type = ce.concurrent.Deferred

  type MVar[F[_], A] = ce.concurrent.MVar[F, A]
  val MVar: ce.concurrent.MVar.type = ce.concurrent.MVar

  type Ref[F[_], A] = ce.concurrent.Ref[F, A]
  val Ref: ce.concurrent.Ref.type = ce.concurrent.Ref

  //----------- handy custom types -----------
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
  val ApplicativeAttempt: effects_impl.ApplicativeAttempt.type = effects_impl.ApplicativeAttempt

  /**
    * Useful since we don't have partial kind application by default
    * Usage:
    * {{{
    *   def canFail[F[_]: MonadAttempt, T](p1: T) : F[T] = ???
    * }}}
    */
  type MonadAttempt[F[_]] = cats.MonadError[F, Throwable]
  val MonadAttempt: effects_impl.MonadAttempt.type = effects_impl.MonadAttempt

  type BracketAttempt[F[_]] = cats.effect.Bracket[F, Throwable]
  val BracketAttempt: effects_impl.BracketAttempt.type = effects_impl.BracketAttempt

  //----------- standard scala types -----------

  //brought in for easy pattern matching. Failure, and Success are used way too often
  //in way too many libraries, so we just alias the std Scala library ones
  type Try[+A] = scala.util.Try[A]
  val Try:        scala.util.Try.type     = scala.util.Try
  val TryFailure: scala.util.Failure.type = scala.util.Failure
  val TrySuccess: scala.util.Success.type = scala.util.Success

  val NonFatal: scala.util.control.NonFatal.type = scala.util.control.NonFatal

  //----------- scala Future -----------
  type Future[+A] = sc.Future[A]
  val Future: sc.Future.type = sc.Future

  type ExecutionContext = sc.ExecutionContext
  val ExecutionContext: sc.ExecutionContext.type = sc.ExecutionContext

  type ExecutionContextExecutor        = sc.ExecutionContextExecutor
  type ExecutionContextExecutorService = sc.ExecutionContextExecutorService

  val Await: sc.Await.type = sc.Await
}
