package com.spotimap.client

import akka.http.scaladsl.model.{FormData, HttpHeader}
import cats.instances.future._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.spotimap.client.SpotifyAlgebra.{Get, PostAsForm}
import com.spotimap.client.TransformUrl.PrependPrefix
import com.spotimap.model.external.SpotifyToken
import io.circe.Decoder

import scala.language.higherKinds

object SpotifyClient {

  private[client] def postFormData[Response: Decoder](path: String, body: FormData, headers: List[HttpHeader])(
    implicit transformUrl: TransformUrl = PrependPrefix): SpotifyAlgebra[Response] = {

    PostAsForm(transformUrl(path), body, headers, Decoder.apply[Response])
  }

  private[client] def get[Response: Decoder](path: String)(
    implicit token: SpotifyToken,
    transformUrl: TransformUrl = PrependPrefix): SpotifyAlgebra[Response] = {

    Get(transformUrl(path), token, Decoder.apply)
  }

}
