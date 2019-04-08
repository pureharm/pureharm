package busymachines.pureharm.core

import scala.annotation.implicitNotFound

/**
  *
  * @tparam T
  *   the type
  * @tparam ID
  *   the value by which our value of type ``T`` can be uniquely identified
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 04 Apr 2019
  *
  */
@implicitNotFound(
  "If a case class T, has a field called 'id of type ID then an Identifiable[T, ID] will be generated for case class, otherwise, please provide one"
)
trait Identifiable[T, ID] {
  def id(t: T): ID

  def fieldName: FieldName
}

object Identifiable extends types.IdentifiableLowPriorityImplicits
