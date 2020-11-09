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
package busymachines.pureharm.internals.rest

import busymachines.pureharm.phantom.SafeSpook
import sttp.tapir

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 21 Jul 2020
  */
object TapirCodecs {

  @inline def safePhantomTypePlainCodec[Underlying, PT](implicit
    tc: tapir.Codec.PlainCodec[Underlying],
    p:  SafeSpook[Throwable, Underlying, PT],
  ): tapir.Codec.PlainCodec[PT] = {
    val m = tapir.Mapping.fromDecode[Underlying, PT](
      f = { u: Underlying =>
        p.spook(u) match {
          case Right(v) => tapir.DecodeResult.Value(v)
          case Left(e)  =>
            tapir.DecodeResult.Error(s"Invalid type format for type=${p.symbolicName}. ${e.getMessage}", e)
        }
      }
    )(
      g = p.despook
    )
    tc.map(m)
  }
}
