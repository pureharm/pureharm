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
    check(value).map(a => unsafe(a))

  /**
    * alias for [[apply]]
    */
  @inline final def spook(value: A): Either[E, Type] =
    apply(value)

  @inline final def despook(spook: Type): A = spook

  /**
    * Please use only when you know what you are doing,
    * like when reading a value from a Database that
    * was always typed with this specific Phantom.
    *
    * If you use it willy nilly sprinkled around your code,
    * relying on a bunch of implicit non-local assumptions
    * then you're a bad software engineer or you are under
    * some heavy deadline.
    */
  @inline final def unsafe(value: A): Type =
    tag[Tag][A](value)
}
