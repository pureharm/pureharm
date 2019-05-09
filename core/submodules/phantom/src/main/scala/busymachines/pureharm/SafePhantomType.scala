package busymachines.pureharm

import shapeless.tag
import tag.@@

/**
  *
  * See [[PhantomType]], but basically, the apply
  * method returns an `Either[E, A]`
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 09 May 2019
  *
  */
trait SafePhantomType[E, A] {
  type Tag = this.type

  final type Type = A @@ Tag

  /**
    * @return
    *   - Right — of the original (or transformed) value to be tagged
    *   - Left — of the failure type you want
    */
  def check(value: A): Either[E, A]

  @inline final def apply(value: A): Either[E, A @@ Tag] =
    check(value).right.map(a => tag[Tag](a))

  /**
    * alias for [[apply]]
    */
  @inline final def spook(value: A): Either[E, A @@ Tag] =
    apply(value)

  @inline final def despook(spook: A @@ Tag): A = spook
}
