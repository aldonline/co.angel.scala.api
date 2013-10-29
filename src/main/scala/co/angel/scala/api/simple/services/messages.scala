package co.angel.scala.api.simple.services.messages

import java.util.Date

import co.angel.scala.api.simple.BaseService

trait Service extends BaseService {
  private def _m( view:String ) = () =>
    client.req(
        "/messages",
        queryParams = Map("view" -> view)
      ).paged[Message]("messages")
    
  def unread = _m("unread")
  def inbox  = _m("inbox")
  def sent   = _m("sent")
  
  def thread( id: String ) = client.req( "/messages/" + id ).one[Thread]
}

case class Thread(
  users: List[User],
  total: Int,
  viewed: Boolean,
  thread_id: String,
  last_message: Option[Message],
  messages:Option[List[Message]]
)

case class Message(
  id:String,
  body:String,
  recipient_id: String,
  sender_id: String,
  created_at: Date,
  viewed: Boolean
)

case class User(
  id: String,
  name: String,
  bio: String,
  follower_count: Int,
  angellist_url:String,
  image:String
)