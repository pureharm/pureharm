package busymachines.pureharm.internals.dbslick

import busymachines.pureharm.dbslick.ConnectionIO
import slick.dbio.{DBIOAction, Effect, NoStream}

/**
  *
  * @author Lorand Szakacs, https://github.com/lorandszakacs
  * @since 01 Aug 2019
  *
  */
object PureharmSlickConnectionIOOps {

  trait Implicits {
    implicit def pureharmSlickWidenCIO[R, S <: NoStream, Eff <: Effect](
      cio: DBIOAction[R, S, Eff],
    ): PureharmSlickWidenCIO[R, S, Eff] = new PureharmSlickWidenCIO(cio)
  }

  final class PureharmSlickWidenCIO[R, S <: NoStream, Eff <: Effect](val cio: DBIOAction[R, S, Eff]) extends AnyVal {

    /**
      * Necessary because type inference kinda fails when you have something like:
      * DBIOAction[R, NoStream, Effect.Read] and you want a ConnectionIO[R] to use all
      * fancy ops. You can easily assign such a type to something of type ConnectionIO[R],
      * but syntax OPS are not being added.
      */
    def widenCIO: ConnectionIO[R] = cio.asInstanceOf[ConnectionIO[R]] //it's always safe to widen
  }

}
