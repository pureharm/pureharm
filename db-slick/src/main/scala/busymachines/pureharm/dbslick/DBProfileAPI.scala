package busymachines.pureharm.dbslick

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 02 Apr 2019
  *
  */
final class DBProfileAPI private (private[dbslick] val api: SlickAPI)

object DBProfileAPI {
  def apply(api: SlickAPI): DBProfileAPI = new DBProfileAPI(api)
}
