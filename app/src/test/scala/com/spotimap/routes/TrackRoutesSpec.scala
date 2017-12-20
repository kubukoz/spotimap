package com.spotimap.routes

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.headers.RawHeader
import cats.instances.future._
import com.kubukoz.spotify.client.api.SpotifyAlgebra
import com.kubukoz.spotify.client.api.SpotifyAlgebra.{Get => SpotifyGet}
import com.kubukoz.spotify.client.impl.SpotifyInterpreter
import com.kubukoz.spotify.client.model.Pager
import com.kubukoz.spotify.client.model.auth.SpotifyToken
import com.kubukoz.spotify.client.model.config.SpotifyConstants.{ApiPrefix, PlayerUrl}
import com.kubukoz.spotify.client.model.player.{Player, PlaylistContext}
import com.kubukoz.spotify.client.model.playlist._
import com.spotimap.Result
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._

class TrackRoutesSpec extends BaseRouteSpec with TrackRoutes {

  private val userToken    = "my-token"
  private val spotifyToken = SpotifyToken.UserToken(userToken)
  private val tokenHeader  = RawHeader("SPOTIFY-TOKEN", userToken)

  "MapRoutes" should {
    "show current playlist tracks" in {
      Get("/tracks/current-tracks").withHeaders(List(tokenHeader)) ~> trackRoutes ~> check {
        status shouldBe OK
        responseAs[List[String]] shouldBe List("Heartbreak", "Garden Dog Barbecue")
      }
    }

    "show current artists" in {
      Get("/tracks/current-artists").withHeaders(List(tokenHeader)) ~> trackRoutes ~> check {
        status shouldBe OK
        //checking as list to make sure there are no duplicates
        responseAs[List[String]] shouldBe List("Artist 1", "Artist 2", "Artist 3")
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
            Pager.complete(
              List(
                Item(Track("Heartbreak", List(Artist("Artist 1"), Artist("Artist 2")))),
                Item(Track("Garden Dog Barbecue", List(Artist("Artist 2"), Artist("Artist 3"))))
              )
            )
          )
        }

      case _ => throw new UnsupportedOperationException
    }
  }
}
