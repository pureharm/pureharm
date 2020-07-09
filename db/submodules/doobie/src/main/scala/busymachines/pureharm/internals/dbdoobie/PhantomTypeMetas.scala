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
package busymachines.pureharm.internals.dbdoobie

import busymachines.pureharm.effects.implicits._
import busymachines.pureharm.phantom.{SafeSpook, Spook}
import doobie.Meta

import io.circe._
import io.circe.parser._
import org.postgresql.util.PGobject

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 24 Sep 2019
  */
trait PhantomTypeMetas {

  implicit final def phantomTypeMeta[Underlying, Phantom](implicit
    spook: Spook[Underlying, Phantom],
    meta:  Meta[Underlying],
  ): Meta[Phantom] = meta.imap(spook.spook)(spook.despook)

  implicit final def safePhatomTypeMeta[Err, Underlying, Phantom](implicit
    spook: SafeSpook[Err, Underlying, Phantom],
    meta:  Meta[Underlying],
  ): Meta[Phantom] = meta.imap(spook.unsafe)(spook.despook)

  implicit def jsonMeta[A](implicit codec: Codec[A]): Meta[A] =
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
