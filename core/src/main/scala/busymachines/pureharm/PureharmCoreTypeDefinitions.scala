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

  final type PhantomType[T]        = phantom.PhantomType[T]
  final type SafePhantomType[E, T] = phantom.SafePhantomType[E, T]
  final type AttemptPhantomType[T] = phantom.SafePhantomType[Throwable, T]

  final type Spook[T, PT] = phantom.Spook[T, PT]
  final val Spook: phantom.Spook.type = phantom.Spook

  final type SafeSpook[E, T, PT] = phantom.SafeSpook[E, T, PT]
  final val SafeSpook: phantom.SafeSpook.type = phantom.SafeSpook
}
