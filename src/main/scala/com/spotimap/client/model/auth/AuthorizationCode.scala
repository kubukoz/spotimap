package com.spotimap.client.model.auth

import akka.http.scaladsl.model.FormData

object AuthorizationCode {

  def apply(authorizationCode: String, redirectUri: String): FormData = FormData(
    "grant_type"   -> "authorization_code",
    "code"         -> authorizationCode,
    "redirect_uri" -> redirectUri
  )
}
