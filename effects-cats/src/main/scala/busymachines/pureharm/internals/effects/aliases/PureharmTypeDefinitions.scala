/**
  * Copyright (c) 2017-2019 BusyMachines
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

import busymachines.pureharm.internals.effects.types

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 13 Jun 2019
  *
  */
trait PureharmTypeDefinitions {

  //----------- handy custom types -----------
  final type Attempt[+R] = types.Attempt[R]
  final val Attempt: Either.type = types.Attempt

  final type AttemptT[F[_], R] = types.AttemptT[F, R]
  final val AttemptT: cats.data.EitherT.type = types.AttemptT
  /**
    * Useful since we don't have partial kind application by default
    * Usage:
    * {{{
    *   def canFail[F[_]: ApplicativeAttempt, T](p1: T) : F[T] = ???
    * }}}
    */
  final type ApplicativeAttempt[F[_]] = types.ApplicativeAttempt[F]
  final val ApplicativeAttempt: _root_.busymachines.pureharm.internals.effects.ApplicativeAttempt.type =
    _root_.busymachines.pureharm.internals.effects.ApplicativeAttempt

  /**
    * Useful since we don't have partial kind application by default
    * Usage:
    * {{{
    *   def canFail[F[_]: MonadAttempt, T](p1: T) : F[T] = ???
    * }}}
    */
  final type MonadAttempt[F[_]] = types.MonadAttempt[F]
  final val MonadAttempt: _root_.busymachines.pureharm.internals.effects.MonadAttempt.type =
    _root_.busymachines.pureharm.internals.effects.MonadAttempt

  final type BracketAttempt[F[_]] = types.BracketAttempt[F]
  final val BracketAttempt: _root_.busymachines.pureharm.internals.effects.BracketAttempt.type =
    _root_.busymachines.pureharm.internals.effects.BracketAttempt

  final type FutureLift[F[_]] = _root_.busymachines.pureharm.internals.effects.FutureLift[F]
  final val FutureLift: _root_.busymachines.pureharm.internals.effects.FutureLift.type =
    _root_.busymachines.pureharm.internals.effects.FutureLift
}
