package com.kubukoz.spotify.client.model.playlist

import com.kubukoz.spotify.client.model.Pager

case class Artist(name: String)

case class Track(name: String, artists: List[Artist])

case class Item(track: Track)

case class Playlist(tracks: Pager[Item])

case class Album(tracks: Pager[Track])
