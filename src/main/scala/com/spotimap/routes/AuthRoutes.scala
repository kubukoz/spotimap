package com.spotimap.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cats.instances.future._
import com.spotimap.client.api.SpotifyApi
import com.spotimap.config.ApplicationConfig
import com.spotimap.util.Implicits.{globalEC, interpretAndConvert}
import com.spotimap.Result
import com.spotimap.client.impl.SpotifyInterpreter
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import io.circe.generic.auto._

trait AuthRoutes {
  implicit protected val interpreter: SpotifyInterpreter[Result]
  implicit protected val config: ApplicationConfig

  val authRoutes: Route = get {
    (pathPrefix("auth") & pathPrefix("code") & parameter("code")) { code =>
      complete {
        //todo do something with the token
        SpotifyApi.auth.token(code)
      }
    }
  }
}
