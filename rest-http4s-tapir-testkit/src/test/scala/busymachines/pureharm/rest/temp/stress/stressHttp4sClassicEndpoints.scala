//package busymachines.pureharm.rest.temp.stress
//
//import busymachines.pureharm.rest.temp._
//
////--------------------------------------------
//object TempHttp4sClassic1 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType1))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType1]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType1).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic2 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType2))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType2]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType2).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic3 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType3))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType3]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType3).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic4 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType4))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType4]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType4).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic5 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType5))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType5]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType5).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic6 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType6))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType6]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType6).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic7 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType7))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType7]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType7).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic8 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType8))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType8]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType8).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic9 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType9))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType9]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType9).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic10 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType10))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType10]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType10).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic11 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType11))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType11]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType11).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic12 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType12))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType12]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType12).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic13 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType13))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType13]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType13).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic14 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType14))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType14]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType14).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic15 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType15))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType15]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType15).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic16 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType16))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType16]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType16).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic17 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType17))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType17]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType17).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic18 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType18))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType18]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType18).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic19 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType19))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType19]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType19).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic20 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType20))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType20]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType20).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic21 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType21))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType21]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType21).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic22 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType22))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType22]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType22).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic23 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType23))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType23]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType23).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic24 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType24))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType24]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType24).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic25 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType25))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType25]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType25).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic26 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType26))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType26]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType26).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic27 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType27))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType27]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType27).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic28 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType28))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType28]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType28).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic29 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType29))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType29]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType29).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic30 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType30))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType30]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType30).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic31 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType31))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType31]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType31).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic32 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType32))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType32]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType32).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic33 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType33))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType33]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType33).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic34 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType34))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType34]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType34).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic35 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType35))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType35]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType35).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic36 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType36))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType36]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType36).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic37 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType37))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType37]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType37).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic38 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType38))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType38]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType38).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic39 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType39))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType39]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType39).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic40 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType40))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType40]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType40).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic41 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType41))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType41]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType41).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic42 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType42))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType42]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType42).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic43 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType43))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType43]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType43).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic44 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType44))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType44]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType44).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic45 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType45))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType45]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType45).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic46 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType46))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType46]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType46).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic47 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType47))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType47]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType47).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic48 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType48))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType48]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType48).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic49 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType49))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType49]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType49).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic50 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType50))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType50]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType50).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic51 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType51))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType51]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType51).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic52 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType52))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType52]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType52).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic53 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType53))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType53]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType53).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic54 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType54))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType54]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType54).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic55 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType55))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType55]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType55).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic56 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType56))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType56]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType56).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic57 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType57))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType57]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType57).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic58 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType58))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType58]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType58).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic59 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType59))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType59]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType59).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic60 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType60))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType60]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType60).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic61 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType61))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType61]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType61).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic62 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType62))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType62]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType62).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic63 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType63))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType63]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType63).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic64 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType64))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType64]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType64).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic65 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType65))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType65]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType65).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic66 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType66))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType66]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType66).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic67 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType67))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType67]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType67).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic68 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType68))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType68]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType68).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic69 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType69))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType69]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType69).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic70 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType70))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType70]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType70).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic71 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType71))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType71]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType71).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic72 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType72))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType72]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType72).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic73 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType73))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType73]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType73).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic74 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType74))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType74]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType74).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic75 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType75))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType75]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType75).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic76 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType76))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType76]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType76).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic77 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType77))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType77]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType77).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic78 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType78))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType78]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType78).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic79 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType79))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType79]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType79).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic80 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType80))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType80]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType80).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic81 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType81))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType81]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType81).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic82 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType82))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType82]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType82).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic83 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType83))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType83]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType83).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic84 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType84))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType84]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType84).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic85 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType85))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType85]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType85).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic86 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType86))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType86]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType86).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic87 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType87))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType87]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType87).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic88 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType88))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType88]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType88).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic89 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType89))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType89]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType89).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic90 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType90))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType90]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType90).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic91 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType91))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType91]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType91).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic92 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType92))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType92]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType92).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic93 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType93))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType93]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType93).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic94 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType94))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType94]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType94).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic95 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType95))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType95]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType95).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic96 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType96))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType96]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType96).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic97 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType97))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType97]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType97).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic98 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType98))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType98]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType98).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
//
////--------------------------------------------
//object TempHttp4sClassic99 {
//  import busymachines.pureharm.rest._
//  import busymachines.pureharm.rest.implicits._
//
//  import org.http4s.dsl.Http4sDsl
//  import busymachines.pureharm.effects._
//  import busymachines.pureharm.effects.implicits._
//
//  def service[F[_]: Sync]: HttpRoutes[F] = {
//    val dsl: Http4sDsl[F] = Http4sDsl[F]
//    import dsl._
//    HttpRoutes.of[F] {
//      case GET -> Root / "test" => Ok(Sync[F].delay(??? : MyOutputType99))
//
//      case req @ POST -> Root / path =>
//        Ok {
//          for {
//            _      <- req.as[MyInputType99]
//            _      <- path.pure[F]
//            result <- (??? : MyOutputType99).pure[F]
//          } yield result
//        }
//
//    }
//  }
//
//}
////--------------------------------------------
