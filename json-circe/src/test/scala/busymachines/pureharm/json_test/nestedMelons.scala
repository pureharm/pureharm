package busymachines.pureharm.json_test

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 11 Jun 2019
  *
  */
sealed private[json_test] trait OutdoorMelon

private[json_test] object OutdoorMelons {

  sealed trait Color

  object Colors {

    case object Green extends Color

  }

  case class WildMelon(
    weight: Int,
    color:  Color,
  ) extends OutdoorMelon

}
