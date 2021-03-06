package com.kubukoz.spotify.client.impl

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.Materializer
import cats.instances.future._
import cats.syntax.flatMap._
import cats.syntax.functor._
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import io.circe.{Decoder, Encoder, Json}

import scala.concurrent.{ExecutionContext, Future}
import scala.language.higherKinds

class HttpClientImpl(implicit system: ActorSystem, am: Materializer, ec: ExecutionContext) extends HttpClient[Future] {

  private val http = Http()

  override private[client] def serialize[T: Encoder](input: T): Future[RequestEntity] =
    Marshal(input).to[RequestEntity]

  override private[client] def httpCallRaw[T: Decoder](method: HttpMethod,
                                                       path: String,
                                                       body: Option[RequestEntity],
                                                       headers: List[HttpHeader]): Future[T] = {

    val request = HttpRequest(
      method = method,
      uri = Uri.parseAbsolute(path),
      headers = headers,
      entity = body.getOrElse(HttpEntity.Empty)
    )

    //shamelessly stolen from ErrorAccumulatingUnmarshaller
    def decode(json: Json) =
      Decoder[T]
        .accumulating(json.hcursor)
        .fold(failures => throw ErrorAccumulatingCirceSupport.DecodingFailures(failures), identity)

    for {
      response <- http.singleRequest(request)
      json     <- Unmarshal(response.entity).to[Json]
      result = decode(json)
    } yield result
  }

}
