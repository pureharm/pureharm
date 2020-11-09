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
package busymachines.pureharm.testkit.util

import scala.reflect.ClassTag

import busymachines.pureharm.effects.Attempt
import busymachines.pureharm.effects.implicits._
import org.scalactic.{source, Prettifier}
import org.scalatest._

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 24 Jun 2020
  */
trait PureharmAssertions {
  import org.scalatest.Assertions._

  def assertFailure[E <: Throwable](a: Attempt[_])(implicit ct: ClassTag[E], pos: source.Position): Assertion =
    assertThrows(a.unsafeGet())

  def interceptFailure[E <: Throwable](a: Attempt[_])(implicit ct: ClassTag[E], pos: source.Position): E =
    intercept[E](a.unsafeGet())

  def assertSuccess[T](a: Attempt[T])(expected: T)(implicit prettifier: Prettifier, pos: source.Position): Assertion =
    a match {
      case Left(e)       => fail(s"excepted Right($e) but was Left($e)")
      case Right(actual) => assertResult(expected)(actual)
    }

  def assertLeft[L](a: Either[L, _])(expected: L)(implicit prettifier: Prettifier, pos: source.Position): Assertion =
    a match {
      case Left(actual) => assertResult(expected)(actual)
      case Right(r)     => fail(s"expected Left($expected) but got: Right($r)")
    }

  def assertRight[R](a: Either[_, R])(expected: R)(implicit prettifier: Prettifier, pos: source.Position): Assertion =
    a match {
      case Left(e)       => fail(s"excepted Right($e) but was Left($e)")
      case Right(actual) => assertResult(expected)(actual)
    }

  def assertSome[T](o: Option[T])(expected: T)(implicit prettifier: Prettifier, pos: source.Position): Assertion =
    assert(o === Option(expected))
}
