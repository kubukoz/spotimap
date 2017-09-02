package com.spotimap

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import spray.json.JsString

trait UserRoutes {
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

  val x: String = "hello world"

  val userRoutes: Route = {
    get {
      complete(JsString(x))
    }
  }
}
