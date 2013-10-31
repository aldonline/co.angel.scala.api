package co.angel.scala.api.simple.services.comments

import java.util.Date

import org.json4s.JArray
import org.json4s.jvalue2extractable

import co.angel.scala.api.simple.BaseService
import co.angel.scala.api.util.values._


object Type extends Enumeration {
  type Type = Value
  val Press, Review, Startup, StartupRole, StatusUpdate = Value
}
import Type._

trait Service extends BaseService {
  
  val _Type = Type
  
  def find( commentable_type:Type, commentable_id:ALId ) =
    client.req(
      url = "/comments",
      queryParams = Map(
          "commentable_type" -> commentable_type.toString,
          "commentable_id" -> commentable_id
          )
    ).arr[Comment]
  
  def create(
      commentable_type:Type,
      commentable_id:ALId,
      comment:String ):Comment =
    client.req(
        url = "/comments",
        method = client.POST,
        bodyParams = Map(
          "commentable_type" -> commentable_type.toString,
          "commentable_id" -> commentable_id,
          "comment" -> comment
         )
      ).one[Comment]
  
  def delete( comment_id:ALId ) =
    client.req(
      url =  "/comments/" + comment_id,
      method = client.DELETE
    ).one[Comment]
  
}

case class Comment (
  id: String,         // ( id tpe comment )
  comment: String,    // ( id comment_content x )
  created_at: Date,   // ( id created_at x )
  user: User          // ( id comment_user x )
)

case class User(
  name: String,           // ( id name x )
  id: String, 
  bio: String,            // ( id bio x )
  follower_count: Int,    // ( id follower_count x )
  angellist_url: String,  // ( id angellist_url )
  image: String           // ( id image x )
)
