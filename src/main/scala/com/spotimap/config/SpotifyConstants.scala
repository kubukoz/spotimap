package com.spotimap.config

import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.model.Uri.{Authority, Host, Path, Query}

/**
  * API urls, etc,
  **/
object SpotifyConstants {
  val ApiPrefix = "https://api.spotify.com"
  val TokensUrl = "https://accounts.spotify.com/api/token"
  val PlayerUrl = "/v1/me/player"

  def redirectUri(implicit config: ApplicationConfig): String =
    s"${config.server.host}:${config.server.port}/auth/code"

  /**
    * Generates the URL for the user to login and accept application access.
    * */
  def loginUrl(scopes: List[Scope.Value])(implicit config: ApplicationConfig): String =
    Uri(
      scheme = "https",
      authority = Authority(
        Host("accounts.spotify.com")
      ),
      Path("/authorize")
    ).withQuery(
        Query(
          "client_id"     -> config.spotify.client.clientId,
          "response_type" -> "code",
          "redirect_uri"  -> redirectUri,
          "scope"         -> scopes.map(_.name).mkString(" ")
        )
      )
      .toString

  object Scope extends Enumeration {
    val `playlist-read-private`       = Value
    val `playlist-read-collaborative` = Value
    val `playlist-modify-public`      = Value
    val `playlist-modify-private`     = Value
    val `streaming`                   = Value
    val `ugc-image-upload`            = Value
    val `user-follow-modify`          = Value
    val `user-follow-read`            = Value
    val `user-library-read`           = Value
    val `user-library-modify`         = Value
    val `user-read-private`           = Value
    val `user-read-birthdate`         = Value
    val `user-read-email`             = Value
    val `user-top-read`               = Value
    val `user-read-playback-state`    = Value
    val `user-modify-playback-state`  = Value
    val `user-read-currently-playing` = Value
    val `user-read-recently-played`   = Value

    implicit class ScopeImplicit(val value: Scope.Value) extends AnyVal {

      def name: String = value.toString.replaceAll("\\$minus", "-")
    }
  }

}
