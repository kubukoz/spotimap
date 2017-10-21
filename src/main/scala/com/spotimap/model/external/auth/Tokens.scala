package com.spotimap.model.external.auth

import io.circe.Decoder

case class Tokens(accessToken: String, tokenType: String, scope: String, expiresIn: Int, refreshToken: String)

object Tokens {

  //todo derive automatically
  implicit val decoder: Decoder[Tokens] = {
    for {
      obj <- Decoder.decodeJsonObject
    } yield
      for {
        accessToken  <- obj.apply("access_token").flatMap(_.asString)
        tokenType    <- obj.apply("token_type").flatMap(_.asString)
        scope        <- obj.apply("scope").flatMap(_.asString)
        expiresIn    <- obj.apply("expires_in").flatMap(_.asNumber).flatMap(_.toInt)
        refreshToken <- obj.apply("refresh_token").flatMap(_.asString)
      } yield Tokens(accessToken, tokenType, scope, expiresIn, refreshToken)
  }.flatMap {
    case Some(tokens) => Decoder.const(tokens)
    case None         => Decoder.failedWithMessage("Fields missing yo")
  }
}
