package com.spotimap.client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods.GET
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import com.spotimap.model.external.SpotifyToken
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import io.circe.Decoder

import scala.concurrent.{ExecutionContext, Future}
import scala.language.higherKinds

abstract class SpotifyClient[F[_]] {
  private[client] def get[T: Decoder](path: String, absolute: Boolean = false)
                                     (implicit token: SpotifyToken): F[T] = {
    httpCallRaw[T](GET, path, absolute)
  }

  protected def httpCallRaw[T: Decoder](method: HttpMethod, path: String, absolute: Boolean)
                                     (implicit token: SpotifyToken): F[T]
}

class SpotifyClientImpl(implicit system: ActorSystem, am: Materializer, ec: ExecutionContext)
  extends SpotifyClient[Future]{

  private val http = Http()

  override protected def httpCallRaw[T: Decoder](method: HttpMethod, path: String, absolute: Boolean)
                                     (implicit token: SpotifyToken): Future[T] = {
    val getUrl: String => String = if(absolute) identity else fullUrl

    val authorization = Authorization(OAuth2BearerToken(token.value))
    val headers: List[HttpHeader] = List(authorization)

    val request = HttpRequest(method, Uri.parseAbsolute(getUrl(path)), headers)

    for {
      response <- http.singleRequest(request)
      result <- Unmarshal(response.entity).to[T]
    } yield result
  }

  private val fullUrl: String => String = "https://api.spotify.com" + _
}
