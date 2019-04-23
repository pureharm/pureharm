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
  * Convenience trait to mix in into your own domain specific
  * modules for easy single-import experiences
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 04 Apr 2019
  *
  */
trait PureharmCoreTypes {
  type FieldName = _root_.busymachines.pureharm.fieldname.FieldName

  val FieldName: _root_.busymachines.pureharm.fieldname.FieldName.type =
    _root_.busymachines.pureharm.fieldname.FieldName

  type Identifiable[T, ID] = _root_.busymachines.pureharm.Identifiable[T, ID]

  val Identifiable: _root_.busymachines.pureharm.Identifiable.type =
    _root_.busymachines.pureharm.Identifiable

  type PhantomType[T] = _root_.busymachines.pureharm.PhantomType[T]
}
