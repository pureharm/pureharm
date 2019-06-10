package busymachines.pureharm.json

/**
  *
  * !!!! WARNING !!!!
  *
  * Import is mutually exclusive with:
  * {{{
  *   import busymachines.pureharm.json.derive._
  * }}}
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 11 Jun 2019
  *
  */
object autoderive extends io.circe.generic.extras.AutoDerivation {}
