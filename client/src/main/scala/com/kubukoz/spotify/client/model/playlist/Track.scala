package com.kubukoz.spotify.client.model.playlist

import cats.instances.option._
import cats.syntax.foldable._
import cats.syntax.traverse._
import com.kubukoz.spotify.client.SpotifyProgram

case class Artist(name: String)

case class Track(name: String, artists: List[Artist])

case class Item(track: Track)

final case class Pager[T](items: List[T], offset: Int, total: Int, previous: Option[String], next: Option[String]) {

  def concat(p2: Pager[T]): Pager[T] = {
    Pager(
      items = items ::: p2.items,
      offset = p2.offset,
      total = total + p2.total,
      previous = previous,
      next = p2.next
    )
  }

  def map[U](f: T => U): Pager[U] = copy(items = items.map(f))

  def consumeAll(fetch: String => SpotifyProgram[Pager[T]]): SpotifyProgram[Pager[T]] = {
    for {
      nextPagerOpt <- next.traverse(fetch)
      nextStepOpt  <- nextPagerOpt.traverse(_.consumeAll(fetch))
    } yield nextStepOpt.foldLeft(this)(_.concat(_))
  }
}

object Pager {
  def complete[T](items: List[T]): Pager[T] = Pager(items, 0, items.size, None, None)
  def empty[T]: Pager[T] = Pager(Nil, 0, 0, None, None)
}

case class Playlist(tracks: Pager[Item])

case class Album(tracks: Pager[Track])
