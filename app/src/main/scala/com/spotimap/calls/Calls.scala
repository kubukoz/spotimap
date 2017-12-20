package com.spotimap.calls

import cats.syntax.applicative._
import com.kubukoz.spotify.client.SpotifyProgram
import com.kubukoz.spotify.client.api.SpotifyApi.{album, playlist, userPlayer}
import com.kubukoz.spotify.client.model.auth.SpotifyToken
import com.kubukoz.spotify.client.model.player.{AlbumContext, PlayerContext, PlaylistContext}
import com.kubukoz.spotify.client.model.playlist._

object Calls {

  def currentArtists(implicit token: SpotifyToken): SpotifyProgram[Set[Artist]] = {
    currentSongs.map(_.flatMap(_.artists).toSet)
  }

  def currentSongs(implicit token: SpotifyToken): SpotifyProgram[List[Track]] = {

    val getItemsPager: PlayerContext => SpotifyProgram[Pager[Track]] = {
      case PlaylistContext(href) =>
        playlist.getByUrl(href).flatMap(_.tracks.consumeAll(playlist.getTracksByUrl)).map(_.map(_.track))

      case AlbumContext(href) =>
        album.getByUrl(href).flatMap(_.tracks.consumeAll(album.getTracksByUrl))

      case _ => Pager.empty[Track].pure[SpotifyProgram]
    }

    for {
      player <- userPlayer.get
      pager  <- getItemsPager(player.context)
    } yield pager.items
  }
}
