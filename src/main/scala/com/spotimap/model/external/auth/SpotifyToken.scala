package com.spotimap.model.external.auth

import com.spotimap.model.external.auth.SpotifyToken.{NoToken, UserToken}

sealed trait SpotifyToken extends Product with Serializable {

  val value: Option[String] = this match {
    case UserToken(t) => Some(t)
    case NoToken      => None
  }
}

object SpotifyToken {

  case class UserToken(token: String) extends SpotifyToken

  case object NoToken extends SpotifyToken
}
