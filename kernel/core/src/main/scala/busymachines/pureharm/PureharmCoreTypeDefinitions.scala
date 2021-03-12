/** Copyright (c) 2019 BusyMachines
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

/** Convenience trait to mix in into your own domain specific
  * modules for easy single-import experiences
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 04 Apr 2019
  */
trait PureharmCoreTypeDefinitions {
  final type FieldName = identifiable.FieldName.Type
  final val FieldName: identifiable.FieldName.type = identifiable.FieldName

  final type Identifiable[T, ID] = identifiable.Identifiable[T, ID]
  final val Identifiable: identifiable.Identifiable.type = identifiable.Identifiable

  @scala.deprecated("Use SproutSub for semantic compat, but Sprout is recommended for stricter newtyping", "0.0.7")
  final type PhantomType[T] = phantom.PhantomType[T]

  @scala.deprecated(
    "Use SproutRefinedSub for semantic compat, but Sprout is recommended for stricter newtyping",
    "0.0.7",
  )
  final type SafePhantomType[E, T] = phantom.SafePhantomType[E, T]

  @scala.deprecated(
    "Use SproutRefinedSubThrow for semantic compat, but Sprout is recommended for stricter newtyping",
    "0.0.7",
  )
  final type AttemptPhantomType[T] = phantom.SafePhantomType[Throwable, T]

  final type Spook[T, PT] = phantom.Spook[T, PT]
  final val Spook: phantom.Spook.type = phantom.Spook

  final type SafeSpook[E, T, PT] = phantom.SafeSpook[E, T, PT]
  final val SafeSpook: phantom.SafeSpook.type = phantom.SafeSpook

  type Sprout[O]                = sprout.Sprout[O]
  type SproutSub[O]             = sprout.SproutSub[O]
  type SproutRefined[O, E]      = sprout.SproutRefined[O, E]
  type SproutRefinedSub[O, E]   = sprout.SproutRefinedSub[O, E]
  type SproutRefinedThrow[O]    = sprout.SproutRefinedThrow[O]
  type SproutRefinedSubThrow[O] = sprout.SproutRefinedSub[O, Throwable]

  type NewType[O, N] = sprout.NewType[O, N]
  val NewType: sprout.NewType.type = sprout.NewType
  type RefinedType[O, N, E] = sprout.RefinedType[O, N, E]
  val RefinedType: sprout.RefinedType.type = sprout.RefinedType
  type RefinedTypeThrow[O, N] = sprout.RefinedTypeThrow[O, N]
  val RefinedTypeThrow: sprout.RefinedTypeThrow.type = sprout.RefinedTypeThrow
}
