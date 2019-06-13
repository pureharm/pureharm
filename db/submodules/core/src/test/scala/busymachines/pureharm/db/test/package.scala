package busymachines.pureharm.db

import busymachines.pureharm.PhantomType

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 13 Jun 2019
  *
  */
package object test {
  object PhantomByte extends PhantomType[Byte]
  type PhantomByte = PhantomByte.Type

  object PhantomInt extends PhantomType[Int]
  type PhantomInt = PhantomInt.Type

  object PhantomLong extends PhantomType[Long]
  type PhantomLong = PhantomLong.Type

  object PhantomBigDecimal extends PhantomType[BigDecimal]
  type PhantomBigDecimal = PhantomBigDecimal.Type

  object PhantomString extends PhantomType[String]
  type PhantomString = PhantomString.Type

  object PhantomPK extends PhantomType[String]
  type PhantomPK = PhantomPK.Type

  object schema {
    val PureharmRows: TableName = TableName("pureharm_rows")
  }

}
