package busymachines.pureharm.internals.dbslick

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 16 Jun 2019
  *
  */
trait SlickAliases {
  final type ProvenShape[U] = slick.lifted.ProvenShape[U]
  final val ProvenShape: slick.lifted.ProvenShape.type = slick.lifted.ProvenShape
}
