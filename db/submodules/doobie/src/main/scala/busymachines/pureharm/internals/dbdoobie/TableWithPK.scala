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
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 24 Sep 2019
  */
abstract class TableWithPK[E, PK](implicit private val iden: Identifiable[E, PK]) {
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

  object row {
    final def pkValueOf(e: E): PK = iden.id(e)

    final val pkColumn: Column =
      Column.internalTag(iden.fieldName)

    lazy val columns: NonEmptyList[Column] = NEList(
      pkColumn,
      cnsWithoutPK,
    )

    lazy val columnNames: List[String] =
      columns.toList.asInstanceOf[List[String]]

    object sql {
      final val QM    = "?"
      final val Comma = ", "

      /**
        * @return
        *  Comma separated enumeration of all columns:
        *  {{{
        *     pk, column1, column 2
        *  }}}
        */
      lazy val tuple: String =
        commaSeparated(columnNames)

      /**
        * @return
        *  Comma seperated enumeration of all given columns:
        *  {{{
        *     pk, column1, column 2
        *  }}}
        */
      def tuple(col: Column, cols: Column*): String =
        commaSeparated(NEList.of(col, cols: _*))

      lazy val tupleInParens: String = s"(${commaSeparated(columnNames)})"

      def tupleInParens(col: Column, cols: Column*): String =
        s"(${tuple(col, cols: _*)})"

      /**
        * @return
        *   Comma separated enumeration of ? corresponding
        *   to the total number of columns in the table
        */
      lazy val qms: String =
        columnNames.map(_ => QM).intercalate(Comma)

      lazy val qmsInParens: String = s"($qms)"

      def columnEqualQM(c: Column): String = s"$c = $QM"

      def tupleEqualQM(c: Column, cs: Column*): String =
        (c :: cs.toList).map(columnEqualQM).intercalate(Comma)

      lazy val tupleEqualQM: String =
        columns.map(columnEqualQM).intercalate(Comma)

      private def commaSeparated(cls: NonEmptyList[Column]): String =
        this.commaSeparated(cls.toList.map(s => s: String))

      private def commaSeparated(cls: List[String]):         String =
        cls.intercalate(", ")
    }
  }

  //====================== Ugly mutable stuff here ============================

  import scala.collection.mutable

  private[this] val orderedColumns: mutable.ListBuffer[Column] = mutable.ListBuffer.empty[Column]

  private lazy val cnsWithoutPK: List[Column] =
    orderedColumns.toList

  /**
    * Relies on side-effects and may pure FP dieties forgive me!!
    *
    * Use only in contexts where evaluation is DETERMINISTIC!
    * i.e. in val definitions, or if you don't care to reuse, shove them in a list
    *
    * This is a limitation of doobie because Read/Write instances
    * depend on the order of your fields in the case class,
    * and your columns should be ordered in the same way.
    *
    * This is why writing tests is imperative!
    *
    * Luckily, usage is restricted on on each individual Table instance
    * so you can only create columns from within a the given table
    *
    * Usage example:
    * {{{
    *    object DoobiePureharmTable extends TableWithPK[PureharmRow, PhantomPK] {
    *     override val name: TableName = schema.PureharmRows
    *
    *     val byte_col:    Column = createColumn("byte")
    *     val int_col:     Column = createColumn("int")
    *     val long_col:    Column = createColumn("long")
    *     val big_decimal: Column = createColumn("big_decimal")
    *     val string_col:  Column = createColumn("string")
    *     val jsonb_col:   Column = createColumn("jsonb_col")
    *     val opt_col:     Column = createColumn("opt_col")
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
             |  val f1 = createColumn("f1")
             |  val f2 = createColumn("f2")
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
