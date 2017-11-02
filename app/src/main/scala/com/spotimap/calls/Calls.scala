package com.spotimap.calls

import cats.syntax.applicative._
import com.kubukoz.spotify.client.SpotifyProgram
import com.kubukoz.spotify.client.api.SpotifyApi.{playlist, userPlayer}
import com.kubukoz.spotify.client.model.auth.SpotifyToken
import com.kubukoz.spotify.client.model.player.{PlayerContext, PlaylistContext}
import com.kubukoz.spotify.client.model.playlist.{Artist, Track}

object Calls {

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
