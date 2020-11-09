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
package busymachines.pureharm.anomaly

/** Some suggested naming conventions are put here so that they're easily accessible.
  * These can also be found in the scaladoc of [[busymachines.pureharm.AnomalyID]]
  *
  * - [[busymachines.pureharm.anomaly.MeaningfulAnomalies.NotFound]]
  *   - range: 000-099; e.g. pone_001, ptwo_076, pthree_099
  *
  * - [[busymachines.pureharm.anomaly.MeaningfulAnomalies.UnauthorizedMsg]]
  *   - range: 100-199; e.g. pone_100, ptwo_176, pthree_199
  *
  * - [[busymachines.pureharm.anomaly.MeaningfulAnomalies.ForbiddenMsg]]
  *   - range: 200-299; e.g. pone_200, ptwo_276, pthree_299
  *
  * - [[busymachines.pureharm.anomaly.MeaningfulAnomalies.DeniedMsg]]
  *   - range: 300-399; e.g. pone_300, ptwo_376, pthree_399
  *
  * - [[busymachines.pureharm.anomaly.MeaningfulAnomalies.InvalidInput]]
  *   - range: 400-499; e.g. pone_400, ptwo_476, pthree_499
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 11 Jun 2019
  */
object MeaningfulAnomalies {

  /** Meaning:
    *
    * "you cannot find something; it may or may not exist, and I'm not going
    * to tell you anything else"
    */
  trait NotFound
  private[pureharm] val NotFoundMsg = "Not found"

  /** Meaning:
    *
    * "something is wrong in the way you authorized, you can try again slightly
    * differently"
    */
  trait Unauthorized
  private[pureharm] val UnauthorizedMsg = "Unauthorized"

  /** Meaning:
    *
    * "it exists, but you're not even allowed to know about that;
    * so for short, you can't find it".
    */
  trait Forbidden
  private[pureharm] val ForbiddenMsg = "Forbidden"

  /** Meaning:
    *
    * "you know it exists, but you are not allowed to see it"
    */
  trait Denied
  private[pureharm] val DeniedMsg = "Denied"

  /** Obviously, whenever some input data is wrong.
    *
    * This one is probably your best friend, and the one you
    * have to specialize the most for any given problem domain.
    * Otherwise you just wind up with a bunch of nonsense, obtuse
    * errors like:
    * - "the input was wrong"
    * - "gee, thanks, more details, please?"
    * - sometimes you might be tempted to use NotFound, but this
    * might be better suited. For instance, when you are dealing
    * with a "foreign key" situation, and the foreign key is
    * the input of the client. You'd want to be able to tell
    * the user that their input was wrong because something was
    * not found, not simply that it was not found.
    *
    * Therefore, specialize frantically.
    */
  trait InvalidInput
  private[pureharm] val InvalidInputMsg = "Invalid input"

  /** Special type of invalid input
    *
    * E.g. when you're duplicating something that ought to be unique,
    * like ids, emails.
    */
  trait Conflict
  private[pureharm] val ConflictMsg = "Conflict"
}

private[pureharm] case object NotFoundAnomalyID extends AnomalyID {
  override val name: String = "0"
}

private[pureharm] case object UnauthorizedAnomalyID extends AnomalyID {
  override val name: String = "1"
}

private[pureharm] case object ForbiddenAnomalyID extends AnomalyID {
  override val name: String = "2"
}

private[pureharm] case object DeniedAnomalyID extends AnomalyID {
  override val name: String = "3"
}

private[pureharm] case object InvalidInputAnomalyID extends AnomalyID {
  override val name: String = "4"
}

private[pureharm] case object ConflictAnomalyID extends AnomalyID {
  override val name: String = "5"
}
