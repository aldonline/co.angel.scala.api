package co.angel.scala.api.simple.services.follows

import java.util.Date
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._
import co.angel.scala.api.util._
import co.angel.scala.api.simple._
import co.angel.scala.api.util.values._


// "user", "startup", "User", "Startup"
// are used throughout the API inconsistently
// using this enumeration shields users from this
// ( we call .toLowerCase when needed )
object Type extends Enumeration {
  type Type = Value
  val Startup, User = Value
}
import Type._

trait Service extends BaseService {

  val _Type = Type
  
  def startFollowing( _type:Type, _id:ALId ): Follow =
    client.req(
      "/follows",
      method = client.POST,
      bodyParams = Map(
          "type" -> _type.toString.toLowerCase,
          "id" -> _id
          )
    ).one[Follow]
  
  
  def stopFollowing( _type:Type, id:ALId ): Follow =
    client.req(
      "/follows",
      method = client.DELETE,
      queryParams = Map(
          "type" -> _type.toString.toLowerCase,
          "id" -> id 
          )
    ).one[Follow]
  
  // GET https://api.angel.co/1/follows/relationship?source_id=671&target_type=User&target_id=2
  def relationship( source_id:ALId, target_type:Type, target_id:ALId ) =
    client.req(
      "/follows/relationship",
      Map(
          "source_id" -> source_id,
          // this is the only case where "User" ( title case )
          // is used instead of "user" ( lower case ). Same goes for "Startup"
          "target_type" -> target_type.toString, 
          "target_id" -> target_id
          )
    ).one[Relationship]
  
  
  import scala.collection.SeqView

  private type O = Object
  private type Users     = SeqView[ User, Seq[_] ]
  private type UsersF    = Function0[ Users ]
  
  private type Startups  = SeqView[ Startup, Seq[_] ]
  private type StartupsF = Function0[ Startups ]
  
  private type Strings   = SeqView[ String, Seq[_] ]
  private type StringsF  = Function0[ Strings ]
  
  def user( id:String ) = new O {
      val followers = new O {
        val users = new UsersF{
          def apply = user_followers( id )
          def ids() = user_followers_ids( id )
        }
      }
      val following = new O {
        val users = new UsersF{
          def apply = user_following( id )
          def ids() = user_following_ids( id )
        }
        val startups = new StartupsF{
          def apply = user_following_startups( id )
          def ids() = user_following_startup_ids( id )
        }
      }
    }
  
  def startup( id:String ) = new O {
      val followers = new O {
        val users = new UsersF{
          def apply = startup_followers( id )
          def ids() = startup_follower_ids( id )
        }
      }
    }
  
  private def user_followers( id:String ) =
    client.req( url = "/users/"+ id + "/followers" ).paged[User]("users")
  
  private def user_followers_ids( id:String ) =
    client.req( url = "/users/"+ id + "/followers/ids" ).paged[String]("ids")
  
  private def startup_followers( id:String ) =
    client.req( url = "/startups/"+ id + "/followers" ).paged[User]("users")
  
  private def startup_follower_ids( id:String ) =
    client.req( url = "/startups/"+ id + "/followers/ids" ).paged[String]("ids")
  
  private def user_following( id:String ) =
    client.req( url = "/users/"+ id + "/following", queryParams = Map( "type" -> "user" ) ).paged[User]("users")    
  
  private def user_following_ids( id:String ) =
    client.req( url = "/users/"+ id + "/following/ids", queryParams = Map( "type" -> "user" ) ).paged[String]("ids")     
  
  private def user_following_startups( id:String ) =
    client.req( url = "/users/"+ id + "/following", queryParams = Map( "type" -> "startup" ) ).paged[Startup]("startups")  
  
  private def user_following_startup_ids( id:String ) =
    client.req( url = "/users/"+ id + "/following/ids", queryParams = Map( "type" -> "startup" ) ).paged[String]("ids") 
}

case class Relationship(
  source: Option[Follow],
  target: Option[Follow]
)

case class Follow(
  id: String,
  created_at: Date
)

case class User(
  name:String,
  id:String,
  bio:String,
  follower_count: Int,
  angellist_url:String,
  image:String
)

case class Startup(
  id:String,
  name:String,
  hidden: Boolean,
  angellist_url:String,
  logo_url: String,
  product_desc: String,
  follower_count: Int,
  company_url:String
)
