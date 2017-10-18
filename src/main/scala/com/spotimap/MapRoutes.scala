package com.spotimap

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.spotimap.client.SpotifyApi
import com.spotimap.directives._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import util.Implicits.globalEC

trait MapRoutes {
  protected val api: SpotifyApi[Main.Result]

  val mapRoutes: Route = {
    spotifyToken { implicit token =>
      get {
        pathPrefix("current-tracks") {
          complete {
            api.player.currentSongs().map(_.map(_.name))
          }
        }
      }
    }
  }
}
