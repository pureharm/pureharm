package busymachines.pureharm.json.test

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 11 Jun 2019
  *
  */
sealed private[test] trait OutdoorMelon

private[test] object OutdoorMelons {

  sealed trait Color

  object Colors {

    case object Green extends Color

  }

  case class WildMelon(
    weight: Int,
    color:  Color,
  ) extends OutdoorMelon

}
