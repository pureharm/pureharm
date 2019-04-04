package busymachines.pureharm.core.types

import busymachines.pureharm.core.{FieldName, Identifiable}

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 04 Apr 2019
  *
  */
trait IdentifiableLowPriorityImplicits {
  import shapeless._
  import shapeless.ops.record._
  import IdentifiableLowPriorityImplicits._

  def apply[T, ID](implicit instance: Identifiable[T, ID]): Identifiable[T, ID] = instance

  implicit def mkIdentifiable[T, ID, IHL <: HList](
    implicit gen: LabelledGeneric.Aux[T, IHL],
    selector:     Selector.Aux[IHL,      Witness.`'id`.T, ID]
  ): Identifiable[T, ID] = new IdentifiableByID[T, ID] {
    override def id(t: T): ID = selector(gen.to(t))
  }

}

private[types] object IdentifiableLowPriorityImplicits {
  private val IdFieldName: FieldName = FieldName("id")
  private trait IdentifiableByID[T, ID] extends Identifiable[T, ID] {
    override def fieldName: FieldName = IdFieldName
  }
}
