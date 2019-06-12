package busymachines.pureharm.phdbslick_test

import db._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 12 Jun 2019
  *
  */
final private[phdbslick_test] case class PureharmRow(
  id:         PhantomPK,
  byte:       PhantomByte,
  int:        PhantomInt,
  long:       PhantomLong,
  bigDecimal: PhantomBigDecimal,
  string:     PhantomString,
)
