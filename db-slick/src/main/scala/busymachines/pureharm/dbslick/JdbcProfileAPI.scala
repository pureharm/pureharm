package busymachines.pureharm.dbslick

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 02 Apr 2019
  *
  */
final class JdbcProfileAPI private (private[dbslick] val api: SlickAPI)

object JdbcProfileAPI {
  def apply(api: SlickAPI): JdbcProfileAPI = new JdbcProfileAPI(api)
}
