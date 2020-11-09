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

/** Nothing from this file should ever escape [[busymachines.pureharm]]
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 11 Jun 2019
  */
private[pureharm] trait AnomalyConstructors[Resulting <: AnomalyBase] {
  def apply(id: AnomalyID): Resulting

  def apply(message: String): Resulting

  def apply(parameters: Anomaly.Parameters): Resulting

  def apply(id: AnomalyID, message: String): Resulting

  def apply(id: AnomalyID, parameters: Anomaly.Parameters): Resulting

  def apply(message: String, parameters: Anomaly.Parameters): Resulting

  def apply(id: AnomalyID, message: String, parameters: Anomaly.Parameters): Resulting

  def apply(a: AnomalyBase): Resulting
}

private[pureharm] trait CatastropheConstructors[Resulting <: Catastrophe] extends AnomalyConstructors[Resulting] {

  def apply(causedBy: Throwable): Resulting

  def apply(message: String, causedBy: Throwable): Resulting

  def apply(id: AnomalyID, message: String, causedBy: Throwable): Resulting

  def apply(id: AnomalyID, parameters: Anomaly.Parameters, causedBy: Throwable): Resulting

  def apply(message: String, parameters: Anomaly.Parameters, causedBy: Throwable): Resulting

  def apply(id: AnomalyID, message: String, parameters: Anomaly.Parameters, causedBy: Throwable): Resulting

  def apply(a: AnomalyBase, causedBy: Throwable): Resulting
}

private[pureharm] trait SingletonAnomalyProduct extends Product with Serializable {
  this: Anomaly =>

  override def productElement(n: Int): Any = n match {
    case 0 => id
    case 1 => message
    case 2 => parameters
    case i => throw new IndexOutOfBoundsException(s"Anomaly has only 3 elements, index 0-2. Trying to get $i")
  }

  override def productArity: Int = 3

  override def canEqual(that: Any): Boolean = that.isInstanceOf[Anomaly]
}
