package busymachines.pureharm

import busymachines.pureharm.phantom._

/** @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 13 Jun 2019
  */
package object identifiable {
  object FieldName extends SproutSub[String]
  type FieldName = FieldName.Type
}
