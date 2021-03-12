/** Copyright (c) 2017-2019 BusyMachines
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
package busymachines.pureharm.phantom

/** See [[PhantomType]], but basically, the apply
  * method returns an `Either[E, A]`
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 09 May 2019
  */
@scala.deprecated("Use SproutRefinedSub for semantic compat, but Sprout is recommended for stricter newtyping", "0.0.7")
trait SafePhantomType[E, T] {
  import shapeless.tag
  import tag.@@

  final type Tag  = this.type
  final type Type = T @@ Tag

  /** @return
    *   - Right — of the original (or transformed) value to be tagged
    *   - Left — of the failure type you want
    */
  def check(value: T): Either[E, T]

  @inline final def apply(v: T): Either[E, Type] =
    check(v).map(a => unsafe(a))

  /** alias for [[apply]]
    */
  @inline final def spook(v: T): Either[E, Type] =
    apply(v)

  @inline final def despook(t: Type): T = t

  /** Please use only when you know what you are doing,
    * like when reading a value from a Database that
    * was always typed with this specific Phantom.
    *
    * If you use it willy nilly sprinkled around your code,
    * relying on a bunch of implicit non-local assumptions
    * then you're a bad software engineer or you are under
    * some heavy deadline.
    */
  @inline final def unsafe(v: T): Type =
    tag[Tag][T](v)

  /** Override for a custom spook instance
    */
  implicit def safeSpookInstance: SafeSpook[E, T, Type] = defaultSpook

  private[this] lazy val defaultSpook: SafeSpook[E, T, Type] = new SafeSpook[E, T, Type] {
    @inline override def spook(a: T): Either[E, Type] = SafePhantomType.this.spook(a)

    @inline override def despook(t: Type): T = SafePhantomType.this.despook(t)

    @inline override def unsafe(a: T): Type = SafePhantomType.this.unsafe(a)

    override lazy val symbolicName: String = SafePhantomType.this.getClass.getSimpleName.stripSuffix("$")
  }
}

/** Use this typeclass to talk generically about SafePhantomType. For instance, the way
  * we define generic circe encoders/decoders is the following (taken from pureharm-json-circe):
  * {{{
  *    //don't like the Show[E] too much :/
  *     implicit final def safePhatomTypeEncoder[E, Underlying, Phantom](implicit
  *       spook:   SafeSpook[E, Underlying, Phantom],
  *       encoder: Encoder[Underlying],
  *     ): Encoder[Phantom] = encoder.contramap(spook.despook)
  *
  *     implicit final def safePhatomTypeDecoder[E, Underlying, Phantom](implicit
  *       spook:   SafeSpook[E, Underlying, Phantom],
  *       decoder: Decoder[Underlying],
  *       show:    Show[E],
  *     ): Decoder[Phantom] = decoder.emap(u => spook.spook(u).leftMap(_.show))
  * }}}
  *
  * @tparam E
  *  the error type for validating your PhantomType
  * @tparam T
  *  the underlying type
  * @tparam PT
  *  the final tagged type, "phantom" type.
  */
trait SafeSpook[E, T, PT] {

  def spook(v: T): Either[E, PT]

  def despook(t: PT): T

  def unsafe(v: T): PT

  def symbolicName: String
}

object SafeSpook {
  @inline def apply[E, T, PT](implicit s: SafeSpook[E, T, PT]): SafeSpook[E, T, PT] = s
}
