/**
  * Copyright (c) 2019 BusyMachines
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
package busymachines.pureharm.internals.config

import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._
import busymachines.pureharm.phantom._
import pureconfig.error.CannotConvert
import pureconfig.{ConfigReader, ConfigWriter}

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 16 Jun 2019
  */
object PureharmConfigInstances {

  trait Implicits extends PhantomTypeInstances

  trait PhantomTypeInstances {

    implicit final def phantomTypeConfigReader[Underlying, Phantom](implicit
      spook:  Spook[Underlying, Phantom],
      reader: ConfigReader[Underlying],
    ): ConfigReader[Phantom] = reader.map(spook.spook)

    implicit final def phantomTypeConfigWriter[Underlying, Phantom](implicit
      spook:  Spook[Underlying, Phantom],
      reader: ConfigWriter[Underlying],
    ): ConfigWriter[Phantom] = reader.contramap(spook.despook)

    implicit final def phantomTypeSafeConfigReader[E, Underlying, Phantom](implicit
      spook:  SafeSpook[E, Underlying, Phantom],
      reader: ConfigReader[Underlying],
      show:   Show[E],
      ushow:  Show[Underlying],
    ): ConfigReader[Phantom] =
      reader.emap(s =>
        spook
          .spook(s)
          .leftMap(e =>
            CannotConvert(value = s.show, toType = s"PhantomType[${s.getClass.getCanonicalName}]", because = e.show)
          )
      )

    implicit final def phantomTypeSafeConfigWriter[E, Underlying, Phantom](implicit
      spook:  SafeSpook[E, Underlying, Phantom],
      writer: ConfigWriter[Underlying],
    ): ConfigWriter[Phantom] = writer.contramap(spook.despook)

  }

}
