package busymachines.pureharm.json.test

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 11 Jun 2019
  *
  */
private[test] case class AnarchistMelon(
  noGods:       Boolean,
  noMasters:    Boolean,
  noSuperTypes: Boolean,
)

sealed private[test] trait Melon {
  def weight: Int
}

private[test] case class WinterMelon(
  fuzzy:  Boolean,
  weight: Int,
) extends Melon

private[test] case class WaterMelon(
  seeds:  Boolean,
  weight: Int,
) extends Melon

private[test] case class PhantomMelon(
  weight:     Weight,
  weights:    Weights,
  weightsSet: WeigthsSet,
  duo:        MelonDuo,
  trio:       MelonTrio,
) extends Melon

private[test] case object SmallMelon extends Melon {
  override val weight: Int = 0
}

sealed private[test] trait Taste

private[test] case object SweetTaste extends Taste

//I ran out of ideas, ok? I'll think of better test data.
private[test] case object SourTaste extends Taste

sealed private[test] trait TastyMelon extends Melon {
  def tastes: Seq[Taste]
}

private[test] case class SquareMelon(
  weight: Int,
  tastes: Seq[Taste],
) extends TastyMelon
