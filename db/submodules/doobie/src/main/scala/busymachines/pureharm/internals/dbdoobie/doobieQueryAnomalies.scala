package busymachines.pureharm.internals.dbdoobie

import busymachines.pureharm.db._
/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 22 Jun 2020
  *
  */

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 24 Sep 2019
  *
  */
final private[dbdoobie] case class DoobieDBEntryNotFoundAnomaly(
  override val pk:       String,
  override val causedBy: Option[Throwable],
) extends DBEntryNotFoundAnomaly(pk, causedBy)

final private[dbdoobie] case class DoobieDBBatchInsertFailedAnomaly(
  override val expectedSize: Int,
  override val actualSize:   Int,
  override val causedBy:     Option[Throwable],
) extends DBBatchInsertFailedAnomaly(expectedSize, actualSize, causedBy)

final private[dbdoobie] case class DoobieDBDeleteByPKFailedAnomaly(
  override val pk: String
) extends DBDeleteByPKFailedAnomaly(pk)
