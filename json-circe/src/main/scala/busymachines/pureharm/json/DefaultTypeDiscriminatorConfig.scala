package busymachines.pureharm.json

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 11 Jun 2019
  *
  */
trait DefaultTypeDiscriminatorConfig {

  /**
    * This exists to give us the default behavior of deserializing sealed trait
    * hierarchies by adding and "_type" field to the json, instead of creating
    * a property for each variant.
    *
    * Unfortunately, this uses the name of each variant as the value for the
    * "_type" field, leaving JSON-value APIs vulnerable to rename refactorings.
    *
    */
  implicit final val defaultDerivationConfiguration: io.circe.generic.extras.Configuration =
    io.circe.generic.extras.Configuration.default
      .withDiscriminator(DefaultTypeDiscriminatorConfig.JsonTypeString)

}

object DefaultTypeDiscriminatorConfig {
  final val JsonTypeString: String = "_type"
}
