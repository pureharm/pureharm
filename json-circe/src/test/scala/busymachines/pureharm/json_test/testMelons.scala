package busymachines.pureharm.json_test

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 11 Jun 2019
  *
  */
private[json_test] case class AnarchistMelon(
  noGods:       Boolean,
  noMasters:    Boolean,
  noSuperTypes: Boolean,
)

sealed private[json_test] trait Melon {
  def weight: Int
}

private[json_test] case class WinterMelon(
  fuzzy:  Boolean,
  weight: Int,
) extends Melon

private[json_test] case class WaterMelon(
  seeds:  Boolean,
  weight: Int,
) extends Melon

private[json_test] case object SmallMelon extends Melon {
  override val weight: Int = 0
}

sealed private[json_test] trait Taste

private[json_test] case object SweetTaste extends Taste

//I ran out of ideas, ok? I'll think of better test data.
private[json_test] case object SourTaste extends Taste

sealed private[json_test] trait TastyMelon extends Melon {
  def tastes: Seq[Taste]
}

private[json_test] case class SquareMelon(
  weight: Int,
  tastes: Seq[Taste],
) extends TastyMelon
