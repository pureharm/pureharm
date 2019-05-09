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
package busymachines.pureharm

/**
  *
  * Example use case:
  * {{{
  * package object api {
  *   object SpecificString extends PhantomType[String]
  *   type SpecificString = SpecificString.Type
  * }
  * }}}
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 02 Apr 2019
  *
  */
trait PhantomType[T] {
  import shapeless.tag
  import tag.@@

  final type Tag  = this.type
  final type Type = T @@ Tag

  /**
    * Override if you want to do pure transformations on your value
    * before tagging.
    */
  @inline def apply(value: T): Type =
    tag[Tag][T](value)

  /**
    * alias for [[apply]]
    */
  @inline final def spook(value: T): Type =
    apply(value)

  @inline final def despook(spook: Type): T = spook
}
