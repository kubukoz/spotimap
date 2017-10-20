package com.spotimap.client

import cats.free.Free.liftF
import cats.syntax.applicative._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.spotimap.SpotifyProgram
import com.spotimap.config.SpotifyConfig.PlayerUrl
import com.spotimap.model.external.SpotifyToken
import com.spotimap.model.external.player.{Player, PlayerContext, PlaylistContext}
import com.spotimap.model.external.playlist.{Playlist, Track}
import io.circe.generic.auto._

import scala.language.higherKinds

object SpotifyApi {

  object userPlayer {
    private def get()(implicit token: SpotifyToken): SpotifyProgram[Player] = liftF {
      SpotifyClient.get[Player](PlayerUrl)
    }

    def currentSongs()(implicit token: SpotifyToken): SpotifyProgram[List[Track]] = {
      val getItems: PlayerContext => SpotifyProgram[List[Track]] = {
        case PlaylistContext(href) =>
          playlist.getByUrl(href).map(_.tracks.items.map(_.track))

        case _ => List.empty[Track].pure[SpotifyProgram]
      }

      for {
        player <- userPlayer.get()
        tracks <- getItems(player.context)
      } yield tracks
    }

    private object playlist {
      def getByUrl(playlistUrl: String)(implicit token: SpotifyToken): SpotifyProgram[Playlist] = liftF {
        SpotifyClient.get[Playlist](playlistUrl, absolute = true)
      }
    }
  }
}
