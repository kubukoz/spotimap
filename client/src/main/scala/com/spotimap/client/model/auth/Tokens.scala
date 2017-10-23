package com.spotimap.client.model.auth

import io.circe.generic.extras.{Configuration, ConfiguredJsonCodec}

@ConfiguredJsonCodec
case class Tokens(accessToken: String, tokenType: String, scope: String, expiresIn: Int, refreshToken: String)

object Tokens {
  private implicit val config: Configuration = Configuration.default.withSnakeCaseKeys
}
