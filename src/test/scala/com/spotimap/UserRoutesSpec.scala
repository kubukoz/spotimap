package com.spotimap

import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{ Matchers, WordSpec }

class UserRoutesSpec extends WordSpec with ScalatestRouteTest with Matchers with UserRoutes {

  private val routes = userRoutes

  "UserRoutes" should {
    "show hello world" in {
      Get(uri = "/") ~> routes ~> check {
        status shouldBe StatusCodes.OK

        contentType shouldBe ContentTypes.`application/json`
        entityAs[String] shouldBe "\"hello world\""
      }
    }
  }
}

