package co.angel.scala.api.simple.services.search

import co.angel.scala.api.simple.BaseService

trait Service extends BaseService {

  def search( query:String ) =
    client.req( "/search", queryParams = Map( "query" -> query ) ).arr[Item]

  def searchSlugs( query:String ) =
    client.req( "/search/slugs", queryParams = Map( "query" -> query ) ).one[Item]
  
  
  private def s(`type`:String)(query:String) =
    client.req( "/search", queryParams = Map( "query" -> query, "type" -> `type` ) ).arr[Item]
    
  val searchUser = s("User")_
  val searchStartup = s("Startup")_
  val searchMarketTag = s( "MarketTag")_
  val searchLocationTag = s("LocationTag")_
  
  
  
}

case class Item(
  id:     String,
  pic:    Option[String],
  url:    String,
  name:   String,
  `type`: String
)