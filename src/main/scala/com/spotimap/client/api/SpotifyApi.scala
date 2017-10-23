package com.spotimap.client.api

import cats.free.Free.liftF
import cats.syntax.applicative._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.spotimap.SpotifyProgram
import com.spotimap.client.model.auth.{AuthorizationCode, SpotifyToken, Tokens}
import com.spotimap.client.model.config.SpotifyConstants.PlayerUrl
import com.spotimap.client.model.config.{SpotifyConfig, SpotifyConstants}
import com.spotimap.client.model.player.{Player, PlayerContext, PlaylistContext}
import com.spotimap.client.model.playlist.{Artist, Playlist, Track}
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

    private def get(implicit token: SpotifyToken): SpotifyProgram[Player] = liftF {
      SpotifyClient.get[Player](PlayerUrl)
    }

    def currentArtists(implicit token: SpotifyToken): SpotifyProgram[Set[Artist]] = {
      currentSongs.map(_.flatMap(_.artists).toSet)
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
