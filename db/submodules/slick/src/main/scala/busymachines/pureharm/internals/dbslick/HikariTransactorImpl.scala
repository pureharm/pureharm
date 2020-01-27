/**
  * Copyright (c) 2019 BusyMachines
  *
  * See company homepage at: https://www.busymachines.com/
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package busymachines.pureharm.internals.dbslick

import busymachines.pureharm.effects._
import busymachines.pureharm.dbslick._
import busymachines.pureharm.effects.implicits._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 02 Apr 2019
  *
  */
final private[pureharm] class HikariTransactorImpl[F[_]] private (
  override val slickAPI: JDBCProfileAPI,
  private val session: Ref[F, DatabaseSession],
  override val slickDB:  DatabaseBackend,
)(
  implicit
  private val F:  Async[F],
  private val fl: ContextShift[F],
) extends Transactor[F] {

  override def run[T](cio: ConnectionIO[T]): F[T] = {
    Async.fromFuture(F.delay(slickDB.run(cio)))
  }

  override def shutdown: F[Unit] = F.delay(slickDB.close())

  override def isConnected: F[Boolean] =
    for {
      s <- session.get
      isClosed <- F.delay(s.conn.isClosed)
    } yield !isClosed

  override def recreateSession: F[Unit] =
    for {
      _ <- closeSession
      newSession <- F.delay(DatabaseSession(slickDB.createSession()))
      _ <- session.set(newSession)
    } yield ()

  override def closeSession: F[Unit] =
    for {
      s <- session.get
      _ <- F.delay(s.close())
    } yield ()

  /**
    * The execution context used to run all blocking database input/output.
    *
    * This is the execution context that slick manages internally. Do not
    * use this unless you know what you are doing.
    */
  override def ioExecutionContext: ExecutionContext = slickDB.ioExecutionContext

}

private[pureharm] object HikariTransactorImpl {

  import busymachines.pureharm.db._
  import slick.util.AsyncExecutor

  import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

  def resource[F[_]: Async: ContextShift](
    dbProfile: JDBCProfileAPI,
  )(
    url:         JDBCUrl,
    username:    DBUsername,
    password:    DBPassword,
    asyncConfig: SlickDBIOAsyncExecutorConfig,
  ): Resource[F, Transactor[F]] = {
    Resource.make(unsafeCreate[F](dbProfile)(url, username, password, asyncConfig))(_.shutdown)
  }

  /**
    * Prefer using [[resource]] unless you know what you are doing.
    */
  def unsafeCreate[F[_]: Async: ContextShift](
    slickProfile: JDBCProfileAPI,
  )(
    url:         JDBCUrl,
    username:    DBUsername,
    password:    DBPassword,
    asyncConfig: SlickDBIOAsyncExecutorConfig,
  ): F[Transactor[F]] = {
    val F = Async[F]

    for {
      hikari <- F.delay {
        val hikariConfig = new HikariConfig()
        hikariConfig.setJdbcUrl(url)
        hikariConfig.setUsername(username)
        hikariConfig.setPassword(password)

        new HikariDataSource(hikariConfig)
      }

      exec <- F.delay(
        AsyncExecutor(
          name           = asyncConfig.prefixName:     String,
          minThreads     = asyncConfig.maxConnections: Int,
          maxThreads     = asyncConfig.maxConnections: Int,
          queueSize      = asyncConfig.queueSize:      Int,
          maxConnections = asyncConfig.maxConnections: Int,
        ),
      )
      slickDB <- F.delay(
        DatabaseBackend(
          slickProfile.Database.forDataSource(
            ds             = hikari,
            maxConnections = Option(asyncConfig.maxConnections),
            executor       = exec,
          ),
        ),
      )
      session <- Ref.of(DatabaseSession(slickDB.createSession()))
    } yield new HikariTransactorImpl(slickProfile, session, slickDB)
  }
}
