package busymachines.pureharm.internals.dbslick

import busymachines.pureharm.db.DBEntryNotFoundAnomaly

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 16 Jun 2019
  *
  */
final private[dbslick] case class SlickDBEntryNotFoundAnomaly(
  override val pk:       String,
  override val causedBy: Option[Throwable],
) extends DBEntryNotFoundAnomaly(pk, causedBy)
