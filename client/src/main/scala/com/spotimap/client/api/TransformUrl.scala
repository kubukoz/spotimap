package com.spotimap.client.api

import com.spotimap.client.model.config.SpotifyConstants.ApiPrefix

sealed trait TransformUrl extends Product with Serializable {

  def apply(s: String): String
}

object TransformUrl {

  case object NoTransform extends TransformUrl {

    override def apply(s: String): String = s
  }

  case object PrependPrefix extends TransformUrl {
    private val prependPrefix: String => String = ApiPrefix + _

    override def apply(s: String): String = prependPrefix(s)
  }
}
