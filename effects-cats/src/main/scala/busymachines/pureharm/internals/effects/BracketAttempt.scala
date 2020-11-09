/** Copyright (c) 2019 BusyMachines
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
package busymachines.pureharm.internals.effects

/** Pseudo companion object for [[busymachines.pureharm.effects.BracketAttempt]]
  * type, alias.
  *
  * The reason this is not implemented using the same pattern as in
  * [[PureharmSyntax.AttemptPseudoCompanionSyntax]] is because we cannot overload the
  * apply method from ``MonadError`` based on type parameters. We'd need to be able
  * to call an apply method with only the effect value, which is not possible,
  * so we simply duplicate whatever is in [[cats.ApplicativeError]], luckily it only
  * has only one method, unlike [[scala.Either]] which has A LOT!
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 03 May 2019
  */
object BracketAttempt {
  def apply[F[_]](implicit i: cats.effect.Bracket[F, Throwable]): cats.effect.Bracket[F, Throwable] = i
}
