package busymachines.pureharm

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 04 Apr 2019
  *
  */
package object core {
  object FieldName extends PhantomType[String]
  type FieldName = FieldName.Type
}
