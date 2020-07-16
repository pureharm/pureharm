package busymachines.pureharm.rest

import busymachines.pureharm.internals

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 16 Jul 2020
  */
trait PureharmRestAliasesAndImplicits
  extends sttp.tapir.Tapir with internals.rest.PureharmTapirAliases with internals.rest.PureharmTapirModelAliases
  with internals.rest.PureharmTapirServerAliases with internals.rest.PureharmHttp4sCirceInstances
  with internals.rest.PureharmRestHttp4sTypeAliases with internals.rest.PureharmRestTapirImplicits {}
