package com.spotimap.client.api

import cats.free.Free.liftF
import cats.syntax.applicative._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.spotimap.SpotifyProgram
import com.spotimap.config.SpotifyConstants.PlayerUrl
import com.spotimap.config.{ApplicationConfig, SpotifyConstants}
import com.spotimap.model.external.auth.{AuthorizationCode, SpotifyToken, Tokens}
import com.spotimap.model.external.player.{Player, PlayerContext, PlaylistContext}
import com.spotimap.model.external.playlist.{Playlist, Track}
import io.circe.generic.auto._

import scala.language.higherKinds

object SpotifyApi {

  object auth {

    def token(authorizationCode: String)(implicit config: ApplicationConfig): SpotifyProgram[Tokens] = liftF {
      implicit val transformUrl = TransformUrl.NoTransform
      val authHeader            = config.spotify.client.authorizationHeader

      SpotifyClient.postFormData[Tokens](
        SpotifyConstants.TokensUrl,
        AuthorizationCode(authorizationCode, config.spotify.redirectUri),
        headers = List(authHeader)
      )
    }
  }

  object userPlayer {

    private def get(implicit token: SpotifyToken): SpotifyProgram[Player] = liftF {
      SpotifyClient.get[Player](PlayerUrl)
    }

    def currentSongs(implicit token: SpotifyToken): SpotifyProgram[List[Track]] = {
      val getItems: PlayerContext => SpotifyProgram[List[Track]] = {
        case PlaylistContext(href) =>
          playlist.getByUrl(href).map(_.tracks.items.map(_.track))

        case _ => List.empty[Track].pure[SpotifyProgram]
      }

      for {
        player <- userPlayer.get
        tracks <- getItems(player.context)
      } yield tracks
    }
  }

  private object playlist {

    def getByUrl(playlistUrl: String)(implicit token: SpotifyToken): SpotifyProgram[Playlist] = liftF {
      implicit val transformUrl = TransformUrl.NoTransform

      SpotifyClient.get[Playlist](playlistUrl)
    }
  }
}
