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
package busymachines.pureharm.json.test.derivetest

import busymachines.pureharm.json._
import busymachines.pureharm.json.implicits._
import busymachines.pureharm.json.test._

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 11 Jun 2019
  */
private[test] object melonsDefaultSemiAutoDecoders {

  implicit val tasteDecoder:          Decoder[Taste]          = derive.enumerationDecoder[Taste]
  implicit val melonDecoder:          Decoder[Melon]          = derive.decoder[Melon]
  implicit val anarchistMelonDecoder: Decoder[AnarchistMelon] = derive.decoder[AnarchistMelon]
}

/**
  */
private[test] object melonsDefaultSemiAutoEncoders {

  implicit val tasteEncoder:          Encoder[Taste]          = derive.enumerationEncoder[Taste]
  implicit val melonEncoder:          Encoder.AsObject[Melon] = derive.encoder[Melon]
  implicit val anarchistMelonEncoder: Encoder[AnarchistMelon] = derive.encoder[AnarchistMelon]
}

/**
  */
private[test] object melonsDefaultSemiAutoCodecs {

  implicit val tasteCodec:          Codec[Taste]          = derive.enumerationCodec[Taste]
  implicit val melonCodec:          Codec[Melon]          = derive.codec[Melon]
  implicit val anarchistMelonCodec: Codec[AnarchistMelon] = derive.codec[AnarchistMelon]

}
