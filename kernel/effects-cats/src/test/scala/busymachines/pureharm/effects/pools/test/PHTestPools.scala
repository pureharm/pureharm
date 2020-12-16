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
package busymachines.pureharm.effects.pools.test

import busymachines.pureharm.effects._

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 15 Jun 2019
  */
final private[test] class PHTestPools[F[_]](
  val dbBlocking:      ExecutionContextCT,
  val dbConnection:    ExecutionContextFT,
  val httpEC:          ExecutionContextFT,
  val single:          ExecutionContextST,
  val blockingShifter: BlockingShifter[F],
)

private[test] object PHTestPools {
  import busymachines.pureharm.effects.implicits._
  import org.scalactic.source.Position
  import org.scalatest.Assertions.assert

  /** @param expectContains
    *   checks to see if the Thread Name executing this function
    *   contains the given string
    * @return
    *   the thread name
    */
  private[test] def assertOnTP(expectContains: String)(implicit pos: Position): IO[String] =
    tn >>= compareThreadName(expectContains)

  private[test] def compareThreadName(expected: String)(tn: String)(implicit pos: Position): IO[String] =
    p(s"should be on '$expected', and we are on: $tn") >> IO(assert(tn.contains(expected))(implicitly, pos)) >> IO(tn)

  private[test] val tn: IO[String] = IO(Thread.currentThread().getName)

  private[test] def p(s: String): IO[Unit] = IO(println(s))

}
