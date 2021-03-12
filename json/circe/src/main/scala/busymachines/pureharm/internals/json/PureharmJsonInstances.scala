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
package busymachines.pureharm.internals.json

import scala.annotation.implicitNotFound

import busymachines.pureharm.effects.Show
import busymachines.pureharm.effects.implicits._
import busymachines.pureharm.phantom._
import io.circe.{Decoder, Encoder}

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 12 Jun 2019
  */
object PureharmJsonInstances {

  trait Implicits {

    implicit final def phatomTypeEncoder[Underlying, Phantom](implicit
      spook:   Spook[Underlying, Phantom],
      encoder: Encoder[Underlying],
    ): Encoder[Phantom] = encoder.contramap(spook.despook)

    implicit final def phatomTypeDecoder[Underlying, Phantom](implicit
      spook:   Spook[Underlying, Phantom],
      decoder: Decoder[Underlying],
    ): Decoder[Phantom] = decoder.map(spook.spook)

    //don't like the Show[E] too much :/
    implicit final def safePhatomTypeEncoder[E, Underlying, Phantom](implicit
      spook:   SafeSpook[E, Underlying, Phantom],
      encoder: Encoder[Underlying],
    ): Encoder[Phantom] = encoder.contramap(spook.despook)

    //don't like the Show[E] too much :/
    implicit final def safePhatomTypeDecoder[E, Underlying, Phantom](implicit
      spook:   SafeSpook[E, Underlying, Phantom],
      decoder: Decoder[Underlying],
      @implicitNotFound(
        """Deriving a io.circe.Decoder[${Phantom}] for safe phantom types, requires you to have a Show[${E}] in scope. If your error is of type Throwable, then pureharm.effects.implicits._ contains a Show instance for it. If you used pureharm style, then myapp.effects._ package should have one"""
      )
      show:    Show[E],
    ): Decoder[Phantom] = decoder.emap(u => spook.spook(u).leftMap(_.show))

    implicit final def pureharmSproutCirceEncoder[Old, New](implicit
      oldType: OldType[Old, New],
      encoder: Encoder[Old],
    ): Encoder[New] = encoder.contramap(oldType.oldType)

    implicit final def pureharmSproutCirceDecoder[Old, New](implicit
      newType: NewType[Old, New],
      decoder: Decoder[Old],
    ): Decoder[New] = decoder.map(newType.newType)

    implicit final def pureharmSproutRefinedCirceDecoder[Old, New, E](implicit
      newType: RefinedType[Old, New, E],
      decoder: Decoder[Old],
      @implicitNotFound(
        """Deriving a io.circe.Decoder[${Phantom}] for refined sprout types, requires you to have a Show[${E}] in scope. If your error is of type Throwable, then pureharm.effects.implicits._ contains a Show instance for it. If you used pureharm style, then myapp.effects._ package should have one"""
      )
      show:    Show[E],
    ): Decoder[New] = decoder.emap(u => newType.newType[Either[E, *]](u).leftMap(_.show))

  }

}
