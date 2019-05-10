package busymachines.pureharm

import busymachines.pureharm.phdbslick.definitions

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 09 May 2019
  *
  */
object dbslick extends definitions.PureharmDBSlickTypeDefinitions {
  type PureharmDBSlickTypeDefinitions = definitions.PureharmDBSlickTypeDefinitions
  type PureharmDBSlickImplicits       = definitions.PureharmDBSlickImplicits
  type SlickQueryAlgebraDefinitions   = phdbslick.SlickQueryAlgebraDefinitions
  type SlickQueryAlgebraTypes         = definitions.SlickQueryAlgebraTypes


  object implicits extends definitions.PureharmDBSlickImplicits
}
