package busymachines.pureharm.dbslick

import scala.concurrent.ExecutionContext

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 02 Apr 2019
  *
  */
trait Transactor[F[_]] {
  def run[T](cio: ConnectionIO[T]): F[T]

  def shutdown: F[Unit]

  /**
    * The execution context used to run all blocking database input/output
    */
  def ioExecutionContext: ExecutionContext

  /**
    * Please use only to compensate for the lacks of this evergrowing
    * API. Prefer to make this wrapper support what you want to do,
    * rather than using this thing.
    *
    * @return
    *   The underlying slick representation of a Database, used to
    *   run your DBIOActions.
    */
  def unsafeUnderlyingDB: SlickDB
}

object Transactor {

  import cats.effect._

  def pgSQLHikari[F[_]: Async](
    url:      JDBCUrl,
    username: DBUsername,
    password: DBPassword,
    config:   DBBlockingIOExecutionConfig
  ): Resource[F, Transactor[F]] = impl.PGSqlHikariTransactorImpl.resource[F](
    url      = url,
    username = username,
    password = password,
    config   = config,
  )
}