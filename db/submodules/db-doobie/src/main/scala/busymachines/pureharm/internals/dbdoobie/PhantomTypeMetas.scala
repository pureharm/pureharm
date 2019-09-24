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

import java.time.LocalDate

import busymachines.pureharm.effects.implicits._

import scala.reflect.runtime.universe.TypeTag
import doobie.Meta
import io.circe._
import io.circe.parser._
import org.postgresql.util.PGobject
import shapeless.tag.@@

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 24 Sep 2019
  *
  */
//FIXME: actually add all primitve types, currently only have the ones required to run tests
trait PhantomTypeMetas {

  implicit def phantomStringMeta[Tag]: Meta[String @@ Tag] =
    Meta.StringMeta.asInstanceOf[Meta[String @@ Tag]]

  implicit def phantomByteMeta[Tag](implicit tt: TypeTag[Byte @@ Tag]): Meta[Byte @@ Tag] =
    Meta.ByteMeta.timap(v => shapeless.tag[Tag](v))(identity)

  implicit def phantomIntMeta[Tag](implicit tt: TypeTag[Int @@ Tag]): Meta[Int @@ Tag] =
    Meta.IntMeta.timap(v => shapeless.tag[Tag](v))(identity)

  implicit def phantomLongMeta[Tag](implicit tt: TypeTag[Long @@ Tag]): Meta[Long @@ Tag] =
    Meta.LongMeta.timap(v => shapeless.tag[Tag](v))(identity)

  implicit def phantomFloatMeta[Tag](implicit tt: TypeTag[Float @@ Tag]): Meta[Float @@ Tag] =
    Meta.FloatMeta.timap(v => shapeless.tag[Tag](v))(identity)

  implicit def phantomDoubleMeta[Tag](implicit tt: TypeTag[Double @@ Tag]): Meta[Double @@ Tag] =
    Meta.DoubleMeta.timap(v => shapeless.tag[Tag](v))(identity)

  implicit def phantomScalaBigDecimalMeta[Tag](implicit tt: TypeTag[BigDecimal @@ Tag]): Meta[BigDecimal @@ Tag] =
    Meta.ScalaBigDecimalMeta.timap(v => shapeless.tag[Tag](v))(identity)

  implicit def phantomBooleanMeta[Tag](implicit tt: TypeTag[Boolean @@ Tag]): Meta[Boolean @@ Tag] =
    Meta.BooleanMeta.timap(v => shapeless.tag[Tag](v))(identity)

  implicit def phantomLocalDateMeta[Tag](implicit tt: TypeTag[LocalDate @@ Tag]): Meta[LocalDate @@ Tag] =
    Meta.JavaTimeLocalDateMeta.timap(v => shapeless.tag[Tag](v))(identity)

  implicit def jsonMeta[A](implicit codec: Codec[A]): Meta[A] =
    Meta.Advanced
      .other[PGobject]("jsonb")
      .imap(a => {
        val json = parse(a.getValue).getOrElse(Json.Null)
        codec
          .decodeJson(json)
          .leftMap { e =>
            new RuntimeException(s"Failed to read JSON from DB. THIS IS A BUG!!! '$e'"): Throwable
          }
          .unsafeGet()
      })(
        (a: A) => {
          val o = new PGobject
          o.setType("jsonb")
          o.setValue(codec(a).noSpaces)
          o
        },
      )

}
