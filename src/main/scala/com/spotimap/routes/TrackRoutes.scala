package com.spotimap.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cats.instances.future._
import com.spotimap.client.SpotifyApi
import com.spotimap.directives._
import com.spotimap.util.Implicits.{convert, globalEC}
import com.spotimap.{Result, SpotifyInterpreter}
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._

trait TrackRoutes {
  implicit val interpreter: SpotifyInterpreter[Result]

  val trackRoutes: Route = {
    spotifyToken { implicit token =>
      pathPrefix("tracks") {
        pathPrefix("current-tracks") {
          get {
            complete {
              SpotifyApi.userPlayer.currentSongs().map(_.map(_.name))
            }
          }
        }
      }
    }
  }
}
