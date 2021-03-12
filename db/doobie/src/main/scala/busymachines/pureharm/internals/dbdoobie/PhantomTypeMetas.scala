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
package busymachines.pureharm.internals.dbdoobie

import scala.reflect.runtime.universe.TypeTag

import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._
import busymachines.pureharm.phantom._
import doobie.{Get, Meta, Put}
import io.circe._
import io.circe.parser._
import org.postgresql.util.PGobject

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 24 Sep 2019
  */
trait PhantomTypeMetas {

  implicit final def pureharmSproutOldTypePut[Underlying, New](implicit
    oldType: OldType[Underlying, New],
    get:     Put[Underlying],
  ): Put[New] = get.contramap(oldType.oldType)

  implicit final def pureharmSproutNewTypeMeta[Underlying, New](implicit
    newType: NewType[Underlying, New],
    meta:    Meta[Underlying],
  ): Meta[New] = meta.imap(newType.newType)(newType.oldType)

  implicit final def pureharmSproutRefinedTypeGet[Old: TypeTag, New: TypeTag, Err](implicit
    refined:   RefinedType[Old, New, Err],
    get:       Get[Old],
    showErr:   Show[Err],
    showUnder: Show[Old],
  ): Get[New] = get.temap(s => refined.newType[Either[Err, *]](s).leftMap(_.show))

  implicit final def phantomTypeMeta[Underlying, Phantom](implicit
    spook: Spook[Underlying, Phantom],
    meta:  Meta[Underlying],
  ): Meta[Phantom] = meta.imap(spook.spook)(spook.despook)

  implicit final def safePhatomGet[Err, Underlying: TypeTag, Phantom: TypeTag](implicit
    spook:     SafeSpook[Err, Underlying, Phantom],
    get:       Get[Underlying],
    showErr:   Show[Err],
    showUnder: Show[Underlying],
  ): Get[Phantom] = get.temap(s => spook.spook(s).leftMap(_.show))

  implicit final def safePhatomPut[Err, Underlying, Phantom](implicit
    spook: SafeSpook[Err, Underlying, Phantom],
    put:   Put[Underlying],
  ): Put[Phantom] = put.contramap(spook.despook)

  def jsonMeta[A](implicit codec: Codec[A]): Meta[A] =
    Meta.Advanced
      .other[PGobject]("jsonb")
      .imap { a =>
        val json = parse(a.getValue).getOrElse(Json.Null)
        codec
          .decodeJson(json)
          .leftMap(e => new RuntimeException(s"Failed to read JSON from DB. THIS IS A BUG!!! '$e'"): Throwable)
          .unsafeGet()
      } { (a: A) =>
        val o = new PGobject
        o.setType("jsonb")
        o.setValue(codec(a).noSpaces)
        o
      }

}
