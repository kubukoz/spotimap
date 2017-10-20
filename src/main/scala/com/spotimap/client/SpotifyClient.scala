package com.spotimap.client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.HttpMethods.{GET, POST}
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import cats.Monad
import cats.instances.future._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.spotimap.Main
import com.spotimap.Main.Result
import com.spotimap.model.external.SpotifyToken
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import io.circe.{Decoder, Encoder}

import scala.concurrent.ExecutionContext
import scala.language.higherKinds

abstract class SpotifyClient[+F[_] : Monad] {
  protected type Serialized

  protected def serialize[T: Encoder]: T => F[Serialized]

  private[client] def get[Response: Decoder](path: String, absolute: Boolean = false)
                                            (implicit token: SpotifyToken): F[Response] = {
    httpCallRaw[Response](GET, path, absolute, body = None)
  }

  private[client] def post[Request: Encoder, Response: Decoder](path: String, body: Request, absolute: Boolean = false)
                                                               (implicit token: SpotifyToken): F[Response] = {
    for {
      entity <- serialize(Encoder[Request])(body)
      result <- httpCallRaw[Response](POST, path, absolute, body = Some(entity))
    } yield result
  }

  protected def httpCallRaw[T: Decoder](method: HttpMethod, path: String, absolute: Boolean, body: Option[Serialized])
                                       (implicit token: SpotifyToken): F[T]
}

class SpotifyClientImpl(implicit system: ActorSystem, am: Materializer, ec: ExecutionContext)
  extends SpotifyClient[Main.Result] {

  override protected type Serialized = RequestEntity

  override protected def serialize[T: Encoder]: T => Result[RequestEntity] =
    Marshal(_).to[RequestEntity]

  private val http = Http()

  override protected def httpCallRaw[T: Decoder](method: HttpMethod, path: String, absolute: Boolean, entity: Option[RequestEntity])
                                                (implicit token: SpotifyToken): Main.Result[T] = {
    val getUrl: String => String = if (absolute) identity else fullUrl

    val authorization = Authorization(OAuth2BearerToken(token.value))
    val headers: List[HttpHeader] = List(authorization)

    val request = HttpRequest(
      method = method,
      uri = Uri.parseAbsolute(getUrl(path)),
      headers = headers,
      entity = entity.getOrElse(HttpEntity.Empty)
    )

    for {
      response <- http.singleRequest(request)
      result <- Unmarshal(response.entity).to[T]
    } yield result
  }

  private val fullUrl: String => String = "https://api.spotify.com" + _
}
