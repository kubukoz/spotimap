package com.kubukoz.spotify.client.model.auth

object Scope extends Enumeration {
  val `playlist-read-private`       = Value
  val `playlist-read-collaborative` = Value
  val `playlist-modify-public`      = Value
  val `playlist-modify-private`     = Value
  val `streaming`                   = Value
  val `ugc-image-upload`            = Value
  val `user-follow-modify`          = Value
  val `user-follow-read`            = Value
  val `user-library-read`           = Value
  val `user-library-modify`         = Value
  val `user-read-private`           = Value
  val `user-read-birthdate`         = Value
  val `user-read-email`             = Value
  val `user-top-read`               = Value
  val `user-read-playback-state`    = Value
  val `user-modify-playback-state`  = Value
  val `user-read-currently-playing` = Value
  val `user-read-recently-played`   = Value

  implicit class ScopeImplicit(val value: Scope.Value) extends AnyVal {

    def name: String = value.toString.replaceAll("\\$minus", "-")
  }
}
