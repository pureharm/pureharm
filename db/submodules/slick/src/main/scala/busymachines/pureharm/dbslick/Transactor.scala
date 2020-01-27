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
package busymachines.pureharm.dbslick

import busymachines.pureharm.db._

import busymachines.pureharm.effects._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 02 Apr 2019
  *
  */
trait Transactor[F[_]] {
  def run[T](cio: ConnectionIO[T]): F[T]

  def shutdown: F[Unit]

  /*
   * Checks if the connection to the database is active
   */
  def isConnected: F[Boolean]

  /*
   * Closes the current session connection to the database
   */
  def closeSession: F[Unit]

  /*
   * Recreates the session with the associated connection to the database (equivalent of closing the session and recreating it)
   */
  def recreateSession: F[Unit]

  /**
    * The execution context used to run all blocking database input/output.
    *
    * This is the execution context that slick manages internally. Do not
    * use this unless you know what you are doing.
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
  val slickDB: DatabaseBackend

  /**
    * Please use only to compensate for the lacks of this evergrowing
    * API. Prefer to make this wrapper support what you want to do,
    * rather than using this thing.
    *
    * @return
    *   The underlying JDBC profile you used to instantiate this
    *   [[Transactor]]. Most likely that one global object in your
    *   project that you instantiated once, and then forgot about.
    *   Now available to import through here for more localized
    *   reasoning in case you need it.
    */
  val slickAPI: JDBCProfileAPI

}

object Transactor {
  import busymachines.pureharm.effects._
  import busymachines.pureharm.internals

  def pgSQLHikari[F[_]: Async: ContextShift](
    dbProfile:    JDBCProfileAPI,
    dbConnection: DBConnectionConfig,
    asyncConfig:  SlickDBIOAsyncExecutorConfig,
  ): Resource[F, Transactor[F]] =
    this.pgSQLHikari[F](
      dbProfile = dbProfile,
    )(
      url         = dbConnection.jdbcURL,
      username    = dbConnection.username,
      password    = dbConnection.password,
      asyncConfig = asyncConfig,
    )

  def pgSQLHikari[F[_]: Async: ContextShift](
    dbProfile: JDBCProfileAPI,
  )(
    url:         JDBCUrl,
    username:    DBUsername,
    password:    DBPassword,
    asyncConfig: SlickDBIOAsyncExecutorConfig,
  ): Resource[F, Transactor[F]] =
    internals.dbslick.HikariTransactorImpl.resource[F](
      dbProfile = dbProfile,
    )(
      url         = url,
      username    = username,
      password    = password,
      asyncConfig = asyncConfig,
    )

  /**
    * Prefer using [[pgSQLHikari]] instead.
    *
    * You really need to know what you are doing and
    * ensure proper cleanup if using this.
    */
  def pgSQLHikariUnsafe[F[_]: Async: ContextShift](
    dbProfile:    JDBCProfileAPI,
    dbConnection: DBConnectionConfig,
    asyncConfig:  SlickDBIOAsyncExecutorConfig,
  ): F[Transactor[F]] =
    this.pgSQLHikariUnsafe[F](
      slickProfile = dbProfile,
    )(
      url         = dbConnection.jdbcURL,
      username    = dbConnection.username,
      password    = dbConnection.password,
      asyncConfig = asyncConfig,
    )

  /**
    * Prefer using [[pgSQLHikari]] instead.
    *
    * You really need to know what you are doing and
    * ensure proper cleanup if using this.
    */
  def pgSQLHikariUnsafe[F[_]: Async: ContextShift](
    slickProfile: JDBCProfileAPI,
  )(
    url:         JDBCUrl,
    username:    DBUsername,
    password:    DBPassword,
    asyncConfig: SlickDBIOAsyncExecutorConfig,
  ): F[Transactor[F]] =
    internals.dbslick.HikariTransactorImpl.unsafeCreate[F](
      slickProfile = slickProfile,
    )(
      url         = url,
      username    = username,
      password    = password,
      asyncConfig = asyncConfig,
    )
}
