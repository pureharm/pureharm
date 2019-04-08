package busymachines.pureharm.core.types

import busymachines.pureharm.core

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
  type FieldName = core.FieldName
  val FieldName: core.FieldName.type = core.FieldName

  type Identifiable[T, ID] = core.Identifiable[T, ID]
  val Identifiable: core.Identifiable.type = core.Identifiable

  type PhantomType[T] = core.PhantomType[T]
}
