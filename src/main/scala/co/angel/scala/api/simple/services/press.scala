package co.angel.scala.api.simple.services.press

import java.util.Date

import co.angel.scala.api.simple.BaseService

import co.angel.scala.api.util.values._

trait Service extends BaseService {
  def for_startup( startup_id:ALId ) =
    client.req(
        "/press",
        queryParams = Map( "startup_id" -> startup_id)
        ).paged[Press]("press")

  def find( id:ALId ) = client.req( "/press/" + id ).one[Press]
  
}

case class Press(
  created_at: Date,
  id: String,
  owner_id: String,
  owner_type: String,
  posted_at: Date,
  snippet: String,
  title: String,
  updated_at: Date,
  url:String
)