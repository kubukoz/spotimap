package com.spotimap.model.external.player

import io.circe.Decoder
import io.circe.generic.semiauto

case class Player(context: PlayerContext)

sealed trait PlayerContext extends Product with Serializable

object PlayerContext {
  private val decoders = Map(
    "playlist" -> semiauto.deriveDecoder[PlaylistContext]
  )

  implicit val decoder: Decoder[PlayerContext] = for {
    contextType <- Decoder[String].prepare(_.downField("type"))
    value <- decoders(contextType)
  } yield value
}

case class PlaylistContext(href: String) extends PlayerContext
