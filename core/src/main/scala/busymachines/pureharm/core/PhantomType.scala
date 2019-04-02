package busymachines.pureharm.core

import shapeless.tag.@@

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
  final type Type = T @@ this.type

  @inline final def apply(value: T): T @@ this.type =
    shapeless.tag[this.type](value)

  /**
    * alias for [[apply]]
    */
  @inline final def spook(value: T): T @@ this.type =
    shapeless.tag[this.type](value)

  @inline final def despook(phantom: T @@ this.type): T =
    identity(phantom)
}
