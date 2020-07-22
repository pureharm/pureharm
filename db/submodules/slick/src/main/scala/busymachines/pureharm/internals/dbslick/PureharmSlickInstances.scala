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
package busymachines.pureharm.internals.dbslick

import scala.reflect.ClassTag

import busymachines.pureharm.effects.implicits._
import busymachines.pureharm.phantom._

/**
  * Unfortunately type inference rarely (if ever) works
  * with something fully generic like
  * {{{
  *     trait LowPriorityPhantomTypeInstances {
  *     final implicit def genericColumnType[Tag, T](implicit ct: ColumnType[T]): ColumnType[T @@ Tag] =
  *       ct.asInstanceOf[ColumnType[T @@ Tag]]
  *   }
  * }}}
  *
  * At some point in time another crack will have to be taken to implement
  * the fully generic version, but for now giving up...
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 12 Jun 2019
  */
object PureharmSlickInstances {

  trait PhantomTypeInstances extends PhantomTypeColumnTypes

  trait PhantomTypeColumnTypes {
    protected val enclosingProfile: slick.jdbc.JdbcProfile

    import enclosingProfile._

    implicit final def phantomTypeColumn[Underlying, Phantom: ClassTag](implicit
      spook:  Spook[Underlying, Phantom],
      column: ColumnType[Underlying],
    ): ColumnType[Phantom] = MappedColumnType.base[Phantom, Underlying](spook.despook, spook.spook)

    implicit final def safePhantomTypeColumn[Err <: Throwable, Underlying, Phantom: ClassTag](implicit
      spook:  SafeSpook[Err, Underlying, Phantom],
      column: ColumnType[Underlying],
    ): ColumnType[Phantom] = MappedColumnType.base[Phantom, Underlying](spook.despook, s => spook.spook(s).unsafeGet())
  }

}
