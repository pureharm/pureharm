package busymachines.pureharm.dbslick.psql.test

import busymachines.pureharm.db.{DBConnectionConfig, DBHost, DBPassword, DBUsername, DatabaseName}

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 27 Jan 2020
  *
  */
object PureharmTestConfig {

  /**
    * All these values come from this file:
    * db/docker-pureharm-postgresql-test.sh
    *
    */
  val dbConfig = DBConnectionConfig(
    host     = DBHost("localhost:20010"),
    dbName   = DatabaseName("pureharm_test"),
    username = DBUsername("pureharmony"),
    password = DBPassword("pureharmony"),
  )

}
