package com.spotimap.routes

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.headers.RawHeader
import cats.instances.future._
import cats.syntax.applicative._
import com.spotimap.Result
import com.spotimap.client.api.SpotifyAlgebra
import com.spotimap.client.api.SpotifyAlgebra.{Get => SpotifyGet}
import com.spotimap.client.impl.SpotifyInterpreter
import com.spotimap.config.SpotifyConstants.{ApiPrefix, PlayerUrl}
import com.spotimap.model.external.auth.SpotifyToken
import com.spotimap.model.external.player.{Player, PlaylistContext}
import com.spotimap.model.external.playlist.{Item, Pager, Playlist, Track}
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._

class TrackRoutesSpec extends BaseRouteSpec with TrackRoutes {

  private val routes       = trackRoutes
  private val userToken    = "my-token"
  private val spotifyToken = SpotifyToken.UserToken(userToken)
  private val tokenHeader  = RawHeader("SPOTIFY-TOKEN", userToken)

  "MapRoutes" should {
    "show current playlist tracks" in {
      Get("/tracks/current-tracks").withHeaders(List(tokenHeader)) ~> routes ~> check {
        status shouldBe OK
        responseAs[List[String]] shouldBe List("Heartbreak", "Garden Dog Barbecue")
      }
    }
  }

  override implicit val interpreter: SpotifyInterpreter[Result] = new SpotifyInterpreter[Result] {
    private val PlayerPath = ApiPrefix + PlayerUrl

    override def apply[A](fa: SpotifyAlgebra[A]): Result[A] = fa match {
      case SpotifyGet(PlayerPath, `spotifyToken`, _) =>
        wrap {
          Player(
            PlaylistContext("playlistUrl")
          )
        }

      case SpotifyGet("playlistUrl", `spotifyToken`, _) =>
        wrap {
          Playlist(
            Pager(
              List(
                Item(Track("Heartbreak")),
                Item(Track("Garden Dog Barbecue"))
              )
            )
          )
        }

      case _ => throw new UnsupportedOperationException
    }
  }

  def wrap[A, B](value: A): Result[B] = value.asInstanceOf[B].pure[Result]
}
