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
package busymachines.pureharm.internals.dbdoobie

import busymachines.pureharm.db._
import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._
import busymachines.pureharm.dbdoobie._
import busymachines.pureharm.identifiable.Identifiable

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 24 Sep 2019
  *
  */
abstract class TableWithPK[E, PK](implicit val iden: Identifiable[E, PK]) {
  def name: TableName

  /**
    * Should be overriden as non implicit since doobie doesn't
    * provide semiauto-derivation so you want to write in your subclasses:
    * {{{
    *   override def readE: Read[MyCaseClass] = Read[MyCaseClass]
    * }}}
    *
    * These are then aliased as implicits in the [[DoobieQueryAlgebra]]
    * for seamless use 99% of the cases
    */
  def showPK: Show[PK]
  def metaPK: Meta[PK]
  def readE:  Read[E]
  def writeE: Write[E]

  final val pkColumn: Column = Column.internalTag(iden.fieldName)

  final def pkOf(e: E): PK = iden.id(e)

  final def tupleString: String =
    columnNames.intercalate(", ")

  final def tupleStringEnclosed: String = s"($tupleString)"

  final private val QM    = "?"
  final private val Comma = ", "

  final def questionMarkTuple: String =
    columnNames.map(_ => QM).intercalate(Comma)

  final def columnEqualQuestionMark(cn: Column): String =
    s"$cn = $QM"

  final def allColumnsEqualQuestionMark: String =
    allCNS.map(columnEqualQuestionMark).intercalate(Comma)

  final def questionMarkTupleEnclosed: String = s"($questionMarkTuple)"
//    s"(${Row.asQuestionMarks(columns)})"

  //========================================
  //========================================
  import scala.collection.mutable

  private[this] val orderedColumns: mutable.ListBuffer[Column] = mutable.ListBuffer.empty[Column]

  lazy val cnsWithoutPK: List[Column] =
    orderedColumns.toList

  lazy val allCNS: NonEmptyList[Column] = NEList(
    pkColumn,
    cnsWithoutPK,
  )

  lazy val columnNames: List[String] =
    allCNS.toList.asInstanceOf[List[String]]

  /**
    * Relies on side-effects and may pure FP dieties forgive me!!
    * Luckily, usage is restricted on on each individual Table instance
    * so you can only create columns from within a the given table
    *
    * Use only to assign to val or lazy val!
    *
    * Usage example:
    * {{{
    *    object DoobiePureharmTable extends TableWithPK[PureharmRow, PhantomPK] {
    *     override val name: TableName = schema.PureharmRows
    *
    *     val byte_col:    ColumnName = createColumn("byte")
    *     val int_col:     ColumnName = createColumn("int")
    *     val long_col:    ColumnName = createColumn("long")
    *     val big_decimal: ColumnName = createColumn("big_decimal")
    *     val string_col:  ColumnName = createColumn("string")
    *     val jsonb_col:   ColumnName = createColumn("jsonb_col")
    *     val opt_col:     ColumnName = createColumn("opt_col")
    *
    *     implicit private[DoobiePureharmRowDAO] val pureharmJSONColMeta: Meta[PureharmJSONCol] =
    *       jsonMeta[PureharmJSONCol](derive.codec[PureharmJSONCol])
    *
    *     override val showPK: Show[PhantomPK]    = Show[PhantomPK]
    *     override val metaPK: Meta[PhantomPK]    = Meta[PhantomPK]
    *     override val readE:  Read[PureharmRow]  = Read[PureharmRow]
    *     override val writeE: Write[PureharmRow] = Write[PureharmRow]
    *   }
    * }}}
    *
    */
  protected[this] def createColumn(s: String): Column = Column(s)

  final type Column = Column.Type

  protected object Column {
    import shapeless.tag
    import tag.@@

    final type Tag  = this.type
    final type Type = String @@ Tag

    private[TableWithPK] def apply(s: String): Column = {
      val newValue: Type = internalTag(s)
      if (orderedColumns.contains(newValue) || iden.fieldName == s) {
        throw new RuntimeException(
          s"""
             |Trying to define column with duplicate name: $s in table:
             |${TableWithPK.this.getClass.getCanonicalName}
             |
             |Unfortunately type-safety does not protect us from this.
             |So you have to be careful to define columns with unique names
             |AND in the order they appear in the case class. Example:
             |
             |case class Row(id: Int, f1: String, f2: String)
             |//Identifiable[Row, Int] is generated automatically
             |//which yields us a column that has name "id" already,
             |//so you don't need to define it yourself.
             |
             |object RowTable extends TableWithPK[Row, Int] {
             |  val f1 = CN("f1")
             |  val f2 = CN("f2")
             |  
             |  //abstract members, elided here
             |}
             |
             |""".stripMargin
        )
      }
      else {
        orderedColumns.+=(newValue)
        newValue
      }

    }

    private[TableWithPK] def internalTag(s: String): Type = tag[Tag][String](s)
  }
}
