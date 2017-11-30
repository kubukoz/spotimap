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
import io.circe.Decoder
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

  private def getByUrl[T: Decoder](url: String)(implicit token: SpotifyToken): SpotifyProgram[T] = liftF {
    implicit val prefix: TransformUrl = TransformUrl.NoTransform
    SpotifyClient.get[T](url)
  }

  object playlist {

    def getByUrl(url: String)(implicit token: SpotifyToken): SpotifyProgram[Playlist] =
      SpotifyApi.getByUrl[Playlist](url)

    def getTracksByUrl(url: String)(implicit token: SpotifyToken): SpotifyProgram[Pager[Item]] =
      SpotifyApi.getByUrl[Pager[Item]](url)
  }

  object album {

    def getByUrl(url: String)(implicit token: SpotifyToken): SpotifyProgram[Album] =
      SpotifyApi.getByUrl[Album](url)

    def getTracksByUrl(url: String)(implicit token: SpotifyToken): SpotifyProgram[Pager[Track]] =
      SpotifyApi.getByUrl[Pager[Track]](url)
  }
}
