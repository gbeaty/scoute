package scoute

import scala.scalajs.js

trait SPARouter[L] extends Router {
  def routes: js.Array[Route[L]]

  val prefix = "#"

  def route(hash: String): Option[L] = {
    if(hash.headOption.exists(_ != '#')) {
      return None
    }

    val dropped = hash.drop(1)
    val paths = dropped.split("/")

    routes.foreach { route =>
      if(route.pattern.length == paths.length) {
        val res = route(paths)
        if(res.isDefined) {
          return res
        }
      }
    }

    None
  }
}