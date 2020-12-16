package busymachines.pureharm

import busymachines.pureharm.phantom.PhantomType

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 13 Jun 2019
  */
package object identifiable {
  object FieldName extends PhantomType[String]
  type FieldName = FieldName.Type
}
