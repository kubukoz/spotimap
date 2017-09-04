package com.spotimap

import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.spotimap.client.SpotifyApi
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

class MapRoutesSpec extends WordSpec with ScalatestRouteTest with Matchers with MapRoutes {

  private val routes = mapRoutes

  "MapRoutes" should {

  }

  override val api: SpotifyApi[Future] = null
}

