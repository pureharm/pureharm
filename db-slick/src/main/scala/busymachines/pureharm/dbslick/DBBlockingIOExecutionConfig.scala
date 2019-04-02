package busymachines.pureharm.dbslick

/**
  *
  * For now we only expose one single configuration,
  * in future versions we'll provide more configurable,
  * and type-safe DSL, so you don't accidentally shoot yourself
  * in the foot by providing a possibly dead-locking config.
  *
  * @param queueSize
  *
  * @param maxConnections
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 02 Apr 2019
  */
final case class DBBlockingIOExecutionConfig(
  prefixName:     String,
  queueSize:      Int,
  maxConnections: Int,
)

object DBBlockingIOExecutionConfig {

  def default: DBBlockingIOExecutionConfig = DBBlockingIOExecutionConfig(
    prefixName     = "pureharm-db",
    queueSize      = 2000,
    maxConnections = 20,
  )
}
