package com.spotimap.client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import cats.instances.future._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.spotimap.Main.Result
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import io.circe.{Decoder, Encoder}

import scala.concurrent.ExecutionContext
import scala.language.higherKinds

class HttpClientImpl(implicit system: ActorSystem, am: Materializer, ec: ExecutionContext)
  extends HttpClient[Result] {

  private val http = Http()

  override private[client] def serialize[T: Encoder]: T => Result[RequestEntity] =
    Marshal(_).to[RequestEntity]

  override private[client] def httpCallRaw[T: Decoder](method: HttpMethod, path: String,
                                                       body: Option[RequestEntity], headers: List[HttpHeader]): Result[T] = {

    val request = HttpRequest(
      method = method,
      uri = Uri.parseAbsolute(path),
      headers = headers,
      entity = body.getOrElse(HttpEntity.Empty)
    )

    for {
      response <- http.singleRequest(request)
      result <- Unmarshal(response.entity).to[T]
    } yield result
  }

}
