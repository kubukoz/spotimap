package com.kubukoz.spotify.client.model.config

import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.model.Uri.{Authority, Host, Path, Query}
import com.kubukoz.spotify.client.model.auth.Scope

/**
  * API urls, etc,
  **/
object SpotifyConstants {
  val ApiPrefix = "https://api.spotify.com"
  val TokensUrl = "https://accounts.spotify.com/api/token"
  val PlayerUrl = "/v1/me/player"

  /**
    * Generates the URL for the user to login and accept application access.
    * */
  def loginUrl(redirectUri: String, scopes: List[Scope.Value])(implicit config: SpotifyConfig): String =
    Uri(
      scheme = "https",
      authority = Authority(
        Host("accounts.spotify.com")
      ),
      Path("/authorize")
    ).withQuery(
        Query(
          "client_id"     -> config.clientId,
          "response_type" -> "code",
          "redirect_uri"  -> redirectUri,
          "scope"         -> scopes.map(_.name).mkString(" ")
        )
      )
      .toString
}
