package co.angel.scala.api.simple.services.likes

import java.util.Date

import co.angel.scala.api.simple.BaseService
import co.angel.scala.api.util.values._

object Type extends Enumeration {
  type Type = Value
  val Comment, Press, Review, StartupRole, StatusUpdate = Value
}
import Type._

trait Service extends BaseService {
  
  val _Type = Type
  
  def find( likable_type:Type, likable_id:ALId ) =
    client.req(
        "/likes",
        queryParams = Map(
            "likable_type" -> likable_type.toString,
            "likable_id" -> likable_id
        )
     ).paged[Like]("likes")
  
  def create( likable_type:Type, likable_id:ALId ) =
    client.req(
        "/likes",
        bodyParams = Map(
            "likable_type" -> likable_type.toString,
            "likable_id" -> likable_id ),
        method = client.POST
        
    ).one[Like]
  
  def delete( like_id: ALId ) = 
    client.req(
      url = "/likes/" + like_id,
      method = client.DELETE    
    ).one[Like]

}

case class Like(
 id:          String,
 likable_id:  String,
 likable_type:String,
 created_at:  Date,
 updated_at:  Date,
 user:        User
)

case class User(
  id:String,
  name:String,
  bio:String,
  follower_count: Int,
  angellist_url:String,
  image:String
)