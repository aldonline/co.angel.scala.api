package co.angel.scala.api.simple.services.startup_roles

// founder, employee, past_investor, current_investor, advisor, incubator and referrer

import java.util.Date

import co.angel.scala.api.simple.BaseService
import co.angel.scala.api.util.values._

trait Service extends BaseService {

  def startup( id:ALId ){
    new Object {
      
      // the tagged element is this startup
      // interesting bit is the other one
      // which can be a startup or a user
      def outgoing = {
        val m = Map[String,String]( "startup_id" -> id, "direction" -> "outgoing" )
        client.req("/startup_roles", queryParams = m )
      }
      
      // the tagged element is something else
      // thats the interesting bit
      def incoming = {
        val m = Map( "startup_id" -> id, "direction" -> "incoming")        
      }
      def outgoing( role:String ) = {
        val m = Map( "startup_id" -> id, "direction" -> "outgoing", "role" -> role )
      }
      def incoming( role:String ) = {
        val m = Map( "startup_id" -> id, "direction" -> "incoming", "role" -> role )
        
      }
    }
  }
  
  /**
   * All roles for a user have tagged=this_user and startup=interesting_startup
   */
  def user( id:ALId ){
    new Object {
      def all( ) = {
        val m = Map[String, String]( "user_id" -> id , "v" -> "1" )
        client.req( "/startup_roles", m ).paged[Role]("startup_roles") // only startups
      }
      def by_role_type( role: String ) = {
        val m = Map[String,String]( "user_id" -> id, "role" -> role, "v" -> "1" )
        client.req( "/startup_roles", m ).paged[Role]("startup_roles") // only startups
      }
    }
  }
}


case class Role(
  id: String,
  role: String,
  created_at: Date,
  started_at: Option[Date],
  ended_at: Option[Date],
  confirmed: Boolean,
  // tagged: UserOrStartup,
  startup: Startup
)


case class Startup(
  id: String,
  hidden: Boolean,
  community_profile: Boolean,
  name: String,
  angellist_url: String,
  logo_url: String,
  thumb_url: String,
  quality: Int,
  product_desc: String,
  high_concept: String,
  follower_count: Int,
  company_url: String,
  created_at: Date,
  updated_at: Date 
)
