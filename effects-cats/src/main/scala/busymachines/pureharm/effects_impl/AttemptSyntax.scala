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

/**
  *
  * This helps mimick operations on the ``Attempt`` using
  * the standard ``Either`` companion, thus making all
  * those ops also available.
  *
  * See [[MonadAttempt]] and [[ApplicativeAttempt]] for further
  * details.
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
