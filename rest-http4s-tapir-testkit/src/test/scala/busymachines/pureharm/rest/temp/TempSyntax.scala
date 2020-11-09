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
package busymachines.pureharm.rest.temp

import busymachines.pureharm.rest._

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 10 Jul 2020
  */
object TempSyntax {
  import sttp.tapir._

  case class PathInputSample(
    s: PHString,
    u: PHUUID,
    l: PHLong,
    i: PHInt,
  )

  def testPhantomParams: SimpleEndpoint[(PathInputSample, PHString, PHUUID), Unit] =
    phEndpoint.get
      .in(
        ("test" /
          path[PHString]("stringPath") /
          path[PHUUID]("uuidPath") /
          path[PHLong]("longPath") /
          path[PHInt]("intPath")).mapTo(PathInputSample)
      )
      .in(query[PHString]("stringQ").description("just a string query param"))
      .in(query[PHUUID]("uuidQ").description("just an uuid query param"))
}
