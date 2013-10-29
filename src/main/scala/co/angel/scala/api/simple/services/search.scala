package co.angel.scala.api.simple.services.search

import java.util.Date
import java.net._

import co.angel.scala.api.simple.BaseService

trait Service extends BaseService {

  def searchUser( query:String ) = searchByType( query, "User" )
  def searchStartup( query:String ) = searchByType( query, "Startup" )
  def searchMarketTag( query:String ) = searchByType( query, "MarketTag" )
  def searchLocationTag( query:String ) = searchByType( query, "LocationTag" )
  
  def search( query:String ) =
    client.req( "/search", queryParams = Map( "query" -> query ) ).arr[Item]
  
  private def searchByType( query:String, `type`:String ) =
    client.req( "/search", queryParams = Map( "query" -> query, "type" -> `type` ) ).arr[Item]
  
  def searchSlugs( query:String ) =
    client.req( "/search/slugs", queryParams = Map( "query" -> query ) ).arr[Item]
  
}

case class Item(
  id:     String,
  pic:    Option[String], // ( id pic x )
  url:    String,         // ( id url x )
  name:   String,         // ( id name x )
  `type`: String          // ( id type x )
)

/*
[
  {
    "id": 108,
    "pic": "https://s3.amazonaws.com/photos.angel.co/users/108-medium?1294803521",
    "url": "http://angel.co/joshu",
    "name": "Joshua Schachter",
    "type": "User"
  },
  {
    "id": 671,
    "pic": "https://s3.amazonaws.com/photos.angel.co/users/671-medium?1321649923",
    "url": "http://angel.co/joshuaxls",
    "name": "Joshua Slayton",
    "type": "User"
  },
  {
    "id": 84,
    "pic": "https://s3.amazonaws.com/photos.angel.co/users/84-medium?1315252032",
    "url": "http://angel.co/joshuabaer",
    "name": "Joshua Baer",
    "type": "User"
  }
]
*/





