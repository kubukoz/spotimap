package com.kubukoz.spotify.client.model.playlist

case class Artist(name: String)

case class Track(name: String, artists: List[Artist])

case class Item(track: Track)

case class Pager(items: List[Item])

case class Playlist(tracks: Pager)
