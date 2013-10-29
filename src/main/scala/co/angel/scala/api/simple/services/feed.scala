package co.angel.scala.api.simple.services.feed

import java.util.Date

import co.angel.scala.api.simple.BaseService

trait Service extends BaseService {
  def all =
    client.req(
        url = "/feed"
      ).paged[Entry]("feed")

  def personalized =
    client.req(
      url = "/feed",
      queryParams = Map( "personalized" -> 1.toString )
    ).paged[Entry]("feed")
  
  // TODO: we need a different kind of pager for this
  def allSince( d:Date ) = null
  def personalizedSince( d:Date ) = null
}


case class Entry (
  id: String,
  timestamp: Date,
  item: Item,
  description: String,
  text: Option[String],
  likes: Int,
  comments: Int,
  actor: Option[Entity],
  target: Option[Entity]
)

case class Entity(
  `type`: String,
  id: String,
  name: String,
  image: String,
  angellist_url: String,
  `system_user?`: Boolean,
  tagline: String
)

case class Item(
  `type`:String,
  ids: List[String]
)

