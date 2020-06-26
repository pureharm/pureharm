package busymachines.pureharm.internals

import busymachines.pureharm.effects._
import busymachines.pureharm.phantom._

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 26 Jun 2020
  *
  */
package object dbdoobie {

  /**
    * Denotes the EC on which connections are managed,
    * backed up by a fixed thread pool with the number of threads
    * equal to the number of connections
    */
  object DoobieConnectionEC extends PhantomType[ExecutionContext] {
    def safe(ec: ExecutionContextFT): this.Type = this.apply(ec)
  }

  type DoobieConnectionEC = DoobieConnectionEC.Type

  /**
    * Denotes the EC on which transactions(dbops) are managed,
    * backed up by a cached thread pool because blocking
    * i/o is executed on this one
    */
  type DoobieBlocker = DoobieBlocker.Type

  object DoobieBlocker extends PhantomType[Blocker] {
    def safe(ec: ExecutionContextCT): this.Type = this(Blocker.liftExecutionContext(ec))
  }

}
