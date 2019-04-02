package busymachines.pureharm.core

import shapeless.tag.@@

/**
  *
  * Example use case:
  * {{{
  * package object api {
  *   object SpecificString extends PhantomType[String]
  *   type SpecificString = SpecificString.Phantom
  * }
  * }}}
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 02 Apr 2019
  *
  */
trait PhantomType[T] {

  protected type Tag <: this.type

  type Phantom = T @@ Tag

  @inline def apply(value: T): @@[T, Tag] =
    shapeless.tag[Tag](value)

  /**
    * alias for [[apply]]
    */
  @inline def spook(value: T): @@[T, Tag] =
    shapeless.tag[Tag](value)

  @inline def despook(phantom: Phantom): T =
    identity(phantom)
}
