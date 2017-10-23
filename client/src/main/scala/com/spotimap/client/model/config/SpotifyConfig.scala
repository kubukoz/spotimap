package com.spotimap.client.model.config

import akka.http.scaladsl.model.headers.{Authorization, BasicHttpCredentials}

case class SpotifyConfig(clientId: String, clientSecret: String) {
  val authorizationHeader = Authorization(BasicHttpCredentials(clientId, clientSecret))
}
