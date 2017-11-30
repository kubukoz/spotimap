package com.kubukoz.spotify.client.api

import cats.free.Free.liftF
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.kubukoz.spotify.client.SpotifyProgram
import com.kubukoz.spotify.client.model.auth.{AuthorizationCode, SpotifyToken, Tokens}
import com.kubukoz.spotify.client.model.config.SpotifyConstants.PlayerUrl
import com.kubukoz.spotify.client.model.config.{SpotifyConfig, SpotifyConstants}
import com.kubukoz.spotify.client.model.player.Player
import com.kubukoz.spotify.client.model.playlist._
import io.circe.generic.auto._

import scala.language.higherKinds

object SpotifyApi {

  object auth {

    def token(authorizationCode: String, redirectUri: String)(implicit config: SpotifyConfig): SpotifyProgram[Tokens] =
      liftF {
        implicit val transformUrl: TransformUrl = TransformUrl.NoTransform
        val authHeader                          = config.authorizationHeader

        SpotifyClient.postFormData[Tokens](
          SpotifyConstants.TokensUrl,
          AuthorizationCode(authorizationCode, redirectUri),
          headers = List(authHeader)
        )
      }
  }

  object userPlayer {

    def get(implicit token: SpotifyToken): SpotifyProgram[Player] = liftF {
      SpotifyClient.get[Player](PlayerUrl)
    }
  }

  object playlist {

    def getByUrl(playlistUrl: String)(implicit token: SpotifyToken): SpotifyProgram[Playlist] = liftF {
      implicit val transformUrl: TransformUrl = TransformUrl.NoTransform

      SpotifyClient.get[Playlist](playlistUrl)
    }

    def getTracksByUrl(playlistUrl: String)(implicit token: SpotifyToken): SpotifyProgram[Pager[Item]] = liftF {
      implicit val transformUrl: TransformUrl = TransformUrl.NoTransform

      SpotifyClient.get[Pager[Item]](playlistUrl)
    }
  }

  object album {

    def getByUrl(albumUrl: String)(implicit token: SpotifyToken): SpotifyProgram[Album] = liftF {
      implicit val transformUrl: TransformUrl = TransformUrl.NoTransform

      SpotifyClient.get[Album](albumUrl)
    }

    def getTracksByUrl(playlistUrl: String)(implicit token: SpotifyToken): SpotifyProgram[Pager[Track]] = liftF {
      implicit val transformUrl: TransformUrl = TransformUrl.NoTransform

      SpotifyClient.get[Pager[Track]](playlistUrl)
    }
  }
}
