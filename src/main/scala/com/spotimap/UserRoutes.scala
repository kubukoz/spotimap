package com.spotimap

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

trait UserRoutes {
  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

  private val helloWorld: String = "hello world"

  val userRoutes: Route = {
    get {
      complete(helloWorld)
    }
  }
}
