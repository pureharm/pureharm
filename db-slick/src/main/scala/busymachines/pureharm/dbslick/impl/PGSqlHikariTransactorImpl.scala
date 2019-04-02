package busymachines.pureharm.dbslick.impl

import cats.implicits._
import cats.effect._
import busymachines.pureharm.dbslick._

import scala.concurrent.ExecutionContext

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 02 Apr 2019
  *
  */
private[dbslick] class PGSqlHikariTransactorImpl[F[_]] private (
  private val db: SlickDBType
)(
  implicit
  private val F: Async[F]
) extends Transactor[F] {

  override def run[T](cio: ConnectionIO[T]): F[T] =
    IO.fromFuture(IO(db.run(cio))).to[F]

  override def shutdown: F[Unit] = F.delay(db.close())

  /**
    * The execution context used to run all blocking database input/output
    */
  override def ioExecutionContext: ExecutionContext = db.ioExecutionContext

  override def unsafeUnderlyingDB: SlickDBType = db
}

private[dbslick] object PGSqlHikariTransactorImpl {

  import slick.jdbc.PostgresProfile
  import slick.util.AsyncExecutor

  import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

  def resource[F[_]: Async](
    url:      JDBCUrl,
    username: DBUsername,
    password: DBPassword,
    config:   DBBlockingIOExecutionConfig
  ): Resource[F, Transactor[F]] = {
    val F = Async[F]

    val transactor: F[Transactor[F]] = for {
      hikari <- F.delay {
                 val hikariConfig = new HikariConfig()
                 hikariConfig.setJdbcUrl(url)
                 hikariConfig.setUsername(username)
                 hikariConfig.setPassword(password)

                 new HikariDataSource(hikariConfig)
               }

      exec <- F.delay(
               AsyncExecutor(
                 name           = config.prefixName:     String,
                 minThreads     = config.maxConnections: Int,
                 maxThreads     = config.maxConnections: Int,
                 queueSize      = config.queueSize:      Int,
                 maxConnections = config.maxConnections: Int,
               )
             )
      db <- F.delay(
             PostgresProfile.api.Database.forDataSource(
               ds             = hikari,
               maxConnections = Option(config.maxConnections),
               executor       = exec
             )
           )
      _ <- F.delay(db.createSession())
    } yield new PGSqlHikariTransactorImpl(db)

    Resource.make(transactor)(_.shutdown)
  }
}
