package com.spotimap.client

import cats.Monad
import cats.syntax.applicative._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.spotimap.model.external.SpotifyToken
import com.spotimap.model.external.player.{Player, PlayerContext, PlaylistContext}
import com.spotimap.model.external.playlist.{Playlist, Track}
import io.circe.generic.auto._

import scala.concurrent.ExecutionContext
import scala.language.higherKinds

final class SpotifyApi[F[+ _]: Monad](implicit client: SpotifyClient[F], ec: ExecutionContext) {

  object player {
    private def get()(implicit token: SpotifyToken): F[Player] =
      client.get[Player]("/v1/me/player")

    def currentSongs()(implicit token: SpotifyToken): F[List[Track]] = {
      val getItems: PlayerContext => F[List[Track]] = {
        case PlaylistContext(href) =>
          playlist.getByUrl(href).map(_.tracks.items.map(_.track))
        case _ => Nil.pure[F]
      }

      for {
        player <- player.get()
        tracks <- getItems(player.context)
      } yield tracks
    }
  }

  private object playlist {
    def getByUrl(playlistUrl: String)(implicit token: SpotifyToken): F[Playlist] = {
      client.get[Playlist](playlistUrl, absolute = true)
    }
  }

}
