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
package busymachines.pureharm.db.test

import busymachines.pureharm.db._
import busymachines.pureharm.effects._
/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 17 Jun 2019
  *
  */
final class DBConnectionConfigTest extends PureharmFixtureTest {

  override def fixture: Resource[IO, Unit] = Resource.pure(())

  override type FixtureParam = Unit

  iotest("read config from default reference.conf") { _ =>
    DBConnectionConfig.default[IO].map { config =>
      assert(
        config == DBConnectionConfig(
          host     = DBHost("localhost:20010"),
          dbName   = DatabaseName("pureharm_test"),
          username = DBUsername("pureharmony"),
          password = DBPassword("pureharmony"),
        ),
      )
    }
  }
}
