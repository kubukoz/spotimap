package com.spotimap

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cats.instances.future._
import com.spotimap.client.{SpotifyApi, SpotifyInterpreter}
import com.spotimap.directives._
import com.spotimap.util.Implicits.{convert, globalEC}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

import scala.concurrent.Future

trait MapRoutes {
  implicit val interpreter: SpotifyInterpreter[Main.Result]

  val mapRoutes: Route = {
    spotifyToken { implicit token =>
      get {
        pathPrefix("current-tracks") {
          complete {
            convert[List[String], Future] {
              SpotifyApi.userPlayer.currentSongs().map(_.map(_.name))
            }
          }
        }
      }
    }
  }
}
