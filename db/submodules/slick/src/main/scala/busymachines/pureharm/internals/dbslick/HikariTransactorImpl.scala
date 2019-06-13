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
import busymachines.pureharm.effects.implicits._
import busymachines.pureharm.dbslick._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 02 Apr 2019
  *
  */
final private[pureharm] class HikariTransactorImpl[F[_]] private (
  override val slickAPI: JDBCProfileAPI,
  override val slickDB:  DatabaseBackend,
)(
  implicit
  private val F:  Async[F],
  private val cs: ContextShift[F],
) extends Transactor[F] {

  /**
    * See:
    * https://github.com/typelevel/cats-effect/pull/546
    *
    * On why we need to always shift when converting from Future
    */
  override def run[T](cio: ConnectionIO[T]): F[T] = {
    slickDB.run(cio).suspendIn[F]
  }

  override def shutdown: F[Unit] = F.delay(slickDB.close())

  /**
    * The execution context used to run all blocking database input/output
    */
  override def ioExecutionContext: ExecutionContext = slickDB.ioExecutionContext
}

private[pureharm] object HikariTransactorImpl {

  import busymachines.pureharm.db._
  import busymachines.pureharm.effects.implicits._
  import slick.util.AsyncExecutor

  import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

  def resource[F[_]: Async: ContextShift](
    dbProfile: JDBCProfileAPI,
  )(
    url:      JDBCUrl,
    username: DBUsername,
    password: DBPassword,
    config:   SlickDBIOAsyncExecutorConfig,
  ): Resource[F, Transactor[F]] = {
    Resource.make(unsafeCreate[F](dbProfile)(url, username, password, config))(_.shutdown)
  }

  /**
    * Prefer using [[resource]] unless you know what you are doing.
    */
  def unsafeCreate[F[_]: Async: ContextShift](
    slickProfile: JDBCProfileAPI,
  )(
    url:      JDBCUrl,
    username: DBUsername,
    password: DBPassword,
    config:   SlickDBIOAsyncExecutorConfig,
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
          name           = config.prefixName:     String,
          minThreads     = config.maxConnections: Int,
          maxThreads     = config.maxConnections: Int,
          queueSize      = config.queueSize:      Int,
          maxConnections = config.maxConnections: Int,
        ),
      )
      slickDB <- F.delay(
        DatabaseBackend(
          slickProfile.Database.forDataSource(
            ds             = hikari,
            maxConnections = Option(config.maxConnections),
            executor       = exec,
          ),
        ),
      )
      _ <- F.delay(slickDB.createSession())
    } yield new HikariTransactorImpl(slickProfile, slickDB)
  }
}
