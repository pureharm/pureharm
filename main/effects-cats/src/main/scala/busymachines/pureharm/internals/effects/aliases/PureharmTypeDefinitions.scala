/** Copyright (c) 2017-2019 BusyMachines
  *
  * See company homepage at: https://www.busymachines.com/
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package busymachines.pureharm.internals.effects.aliases

import busymachines.pureharm.internals.effects
import busymachines.pureharm.internals.effects.types

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 13 Jun 2019
  */
trait PureharmTypeDefinitions {

  //----------- handy custom types -----------
  final type Attempt[+R] = types.Attempt[R]
  final val Attempt: Either.type = types.Attempt

  final type AttemptT[F[_], R] = types.AttemptT[F, R]
  final val AttemptT: cats.data.EitherT.type = types.AttemptT
  /** Useful since we don't have partial kind application by default
    * Usage:
    * {{{
    *   def canFail[F[_]: ApplicativeAttempt, T](p1: T) : F[T] = ???
    * }}}
    */
  final type ApplicativeAttempt[F[_]] = types.ApplicativeAttempt[F]
  final val ApplicativeAttempt: effects.ApplicativeAttempt.type = effects.ApplicativeAttempt

  /** Useful since we don't have partial kind application by default
    * Usage:
    * {{{
    *   def canFail[F[_]: MonadAttempt, T](p1: T) : F[T] = ???
    * }}}
    */
  final type MonadAttempt[F[_]] = types.MonadAttempt[F]
  final val MonadAttempt: effects.MonadAttempt.type = effects.MonadAttempt

  final type BracketAttempt[F[_]] = types.BracketAttempt[F]
  final val BracketAttempt: effects.BracketAttempt.type = effects.BracketAttempt

  /** Used to block on an F[A], and ensure that all recovery and
    * shifting back is always done.
    *
    * For instance, always ensure that any F[A] that
    * talks to, say, amazon S3, is wrapped in such
    * a
    * {{{
    *   blockingShifter.blockOn(S3Util.putSomething(...))
    * }}}
    *
    * Libraries in the typelevel eco-system tend to already do
    * this, so you don't need to be careful. For instance,
    * doobie will always ensure that this is done to and
    * from the EC that you provide specifically for accessing the
    * DB. But you always need to double check, and be careful
    * that you NEVER execute blocking IO on the same thread pool
    * as the CPU bound one dedicated to your ContextShift[A]
    *
    * @author Lorand Szakacs, https://github.com/lorandszakacs
    * @since 13 Jun 2019
    */
  final type BlockingShifter[F[_]] = effects.BlockingShifter[F]
  final val BlockingShifter: effects.BlockingShifter.type = effects.BlockingShifter

  final type PureharmIOApp = effects.PureharmIOApp
}
