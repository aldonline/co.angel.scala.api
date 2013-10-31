package co.angel.scala.api.simple.services.search

import co.angel.scala.api.util.values._
import co.angel.scala.api.simple.BaseService

trait Service extends BaseService {

  def search( query:String ) =
    client.req( "/search", queryParams = Map( "query" -> query ) ).arr[Item]

  def search( slug:ALSlug ) =
    client.req( "/search/slugs", queryParams = Map( "query" -> slug ) ).one[Item]
  
  private def s(`type`:String)(query:String) =
    client.req( "/search", queryParams = Map( "query" -> query, "type" -> `type` ) ).arr[Item]
    
  val search_user = s("User")_
  val search_start = s("Startup")_
  val search_market_tag = s( "MarketTag")_
  val search_location_tag = s("LocationTag")_
  
}

case class Item(
  id:     String,
  pic:    Option[String],
  url:    String,
  name:   String,
  `type`: String
)