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
package busymachines.pureharm.config.test

/**
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 16 Jun 2019
  */
import busymachines.pureharm.config._
import busymachines.pureharm.effects._
import busymachines.pureharm.effects.implicits._

private[test] class PureharmTestConfigLoaderFromSource(filePath: String) extends ConfigLoader[PureharmTestConfig] {

  override def configSource[F[_]](implicit F: Sync[F]): F[ConfigSource] =
    F.delay(java.nio.file.Paths.get(this.getClass.getClassLoader.getResource(filePath).toURI))
      .map(ConfigSource.file)

  override def configReader: ConfigReader[PureharmTestConfig] = PureharmTestConfig.configReader
}
