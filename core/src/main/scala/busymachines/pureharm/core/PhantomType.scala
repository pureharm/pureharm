package busymachines.pureharm.core

import shapeless.tag
import tag.@@

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
  protected type Tag <: this.type
  final type Type = T @@ Tag

  @inline final def apply(value: T): T @@ Tag =
    tag[Tag](value)

  /**
    * alias for [[apply]]
    */
  @inline final def spook(value: T): T @@ Tag =
    tag[Tag](value)

  @inline final def despook(spook: T @@ Tag): T = spook
}
