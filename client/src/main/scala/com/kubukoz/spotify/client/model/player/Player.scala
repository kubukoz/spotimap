package com.kubukoz.spotify.client.model.player

import io.circe.Decoder
import io.circe.generic.semiauto

case class Player(context: PlayerContext)

sealed trait PlayerContext extends Product with Serializable

object PlayerContext {
  private val decoders = Map(
    "playlist" -> semiauto.deriveDecoder[PlaylistContext],
    "album"    -> semiauto.deriveDecoder[AlbumContext]
  )

  implicit val decoder: Decoder[PlayerContext] = for {
    contextType <- Decoder[String].prepare(_.downField("type"))
    value       <- decoders(contextType)
  } yield value
}

case class PlaylistContext(href: String) extends PlayerContext

case class AlbumContext(href: String) extends PlayerContext
