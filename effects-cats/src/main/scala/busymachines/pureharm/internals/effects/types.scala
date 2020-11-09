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
package busymachines.pureharm.internals.effects

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jun 2019
  */
object types {

  type Attempt[+R] = Either[Throwable, R]
  val Attempt: Either.type = Either

  type AttemptT[F[_], R] = cats.data.EitherT[F, Throwable, R]
  val AttemptT: cats.data.EitherT.type = cats.data.EitherT
  /** Useful since we don't have partial kind application by default
    * Usage:
    * {{{
    *   def canFail[F[_]: ApplicativeAttempt, T](p1: T) : F[T] = ???
    * }}}
    */
  type ApplicativeAttempt[F[_]] = cats.ApplicativeError[F, Throwable]

  /** Useful since we don't have partial kind application by default
    * Usage:
    * {{{
    *   def canFail[F[_]: MonadAttempt, T](p1: T) : F[T] = ???
    * }}}
    */
  type MonadAttempt[F[_]] = cats.MonadError[F, Throwable]

  type BracketAttempt[F[_]] = cats.effect.Bracket[F, Throwable]

}
