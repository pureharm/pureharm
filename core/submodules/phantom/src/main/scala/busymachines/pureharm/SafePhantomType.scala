package busymachines.pureharm

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
  import shapeless.tag
  import tag.@@

  final type Tag  = this.type
  final type Type = A @@ Tag

  /**
    * @return
    *   - Right — of the original (or transformed) value to be tagged
    *   - Left — of the failure type you want
    */
  def check(value: A): Either[E, A]

  @inline final def apply(value: A): Either[E, Type] =
    check(value).right.map(a => tag[Tag][A](a))

  /**
    * alias for [[apply]]
    */
  @inline final def spook(value: A): Either[E, Type] =
    apply(value)

  @inline final def despook(spook: Type): A = spook
}
