package busymachines.pureharm.db.test

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 12 Jun 2019
  *
  */
final private[pureharm] case class PureharmRow(
  id:         PhantomPK,
  byte:       PhantomByte,
  int:        PhantomInt,
  long:       PhantomLong,
  bigDecimal: PhantomBigDecimal,
  string:     PhantomString,
)
