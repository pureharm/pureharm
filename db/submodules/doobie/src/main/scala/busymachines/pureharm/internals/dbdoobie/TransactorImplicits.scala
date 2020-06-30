package busymachines.pureharm.internals.dbdoobie

import busymachines.pureharm.db._
import busymachines.pureharm.dbdoobie._
import doobie.hikari.HikariTransactor
import busymachines.pureharm.effects._

/**
  * There is little point in using something other than a
  * Hikari Transactor for this...
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 26 Jun 2020
  */
trait TransactorImplicits {

  implicit class TransStuff(t: Transactor.type) {

    def pureharmTransactor[F[_]: Async: ContextShift](
      dbConfig:  DBConnectionConfig,
      dbConnEC:  DoobieConnectionEC,
      dbBlocker: DoobieBlocker,
    ): Resource[F, Transactor[F]] =
      TransactorImplicits.pureharmTransactor[F](dbConfig, dbConnEC, dbBlocker)
  }

}

private[pureharm] object TransactorImplicits {

  def pureharmTransactor[F[_]: Async: ContextShift](
    dbConfig:  DBConnectionConfig,
    dbConnEC:  DoobieConnectionEC,
    dbBlocker: DoobieBlocker,
  ): Resource[F, Transactor[F]] =
    for {
      transactor <- HikariTransactor.newHikariTransactor(
        driverClassName = "org.postgresql.Driver",
        url             = dbConfig.jdbcURL,
        user            = dbConfig.username,
        pass            = dbConfig.password,
        connectEC       = dbConnEC,
        blocker         = dbBlocker,
      )
    } yield transactor
}
