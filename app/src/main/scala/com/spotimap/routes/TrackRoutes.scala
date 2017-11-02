package com.spotimap.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cats.instances.future._
import com.kubukoz.spotify.client.impl.SpotifyInterpreter
import com.spotimap.Result
import com.spotimap.calls.Calls
import com.spotimap.directives._
import com.spotimap.util.Implicits.{globalEC, interpretAndConvert}
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._

trait TrackRoutes {
  implicit protected val interpreter: SpotifyInterpreter[Result]

  val trackRoutes: Route = {
    spotifyToken { implicit token =>
      pathPrefix("tracks") {
        pathPrefix("current-tracks") {
          get {
            complete {
              Calls.currentSongs.map(_.map(_.name))
            }
          }
        } ~ pathPrefix("current-artists") {
          get {
            complete {
              Calls.currentArtists.map(_.map(_.name))
            }
          }
        }
      }
    }
  }
}
