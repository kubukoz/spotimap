package com.spotimap.model.external.playlist

case class Track(name: String)
case class Item(track: Track)
case class Pager(items: List[Item])
case class Playlist(tracks: Pager)
