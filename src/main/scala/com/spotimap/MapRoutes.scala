package com.spotimap

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cats.instances.future._
import com.spotimap.client.SpotifyApi
import com.spotimap.directives._
import com.spotimap.util.Implicits.{convert, globalEC}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

trait MapRoutes {
  implicit val interpreter: SpotifyInterpreter[Result]

  val mapRoutes: Route = {
    spotifyToken { implicit token =>
      get {
        pathPrefix("current-tracks") {
          complete {
            SpotifyApi.userPlayer.currentSongs().map(_.map(_.name))
          }
        }
      }
    }
  }
}
