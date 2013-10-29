package co.angel.scala.api.oo

import co.angel.scala.api.simple.Api
import co.angel.scala.api.simple.services._

import java.net.URL
import scala.collection._

trait OOApi { root =>
  
  def api:Api
  
  // the logged in user
  lazy val me = api.users.me
  
  // the logged in users's feed
  def feed = api.feed
  // TODO: what do services return when an item is not found?
  // a 404 of course. we need to surface that ( via Option )
  def startupById( id:String ):Option[Startup] // = api.startups.get( id ):Option[Startup]
  def userById( id:String ):Option[User] //   = api.users.get( id ):Option[User]
  
  // def searchUsers( query:String ): SeqView[UserSearchResult,Seq[_]] = { }
  
  trait UserSearchResult {
  }
  
  trait Comment {
    protected def src: comments.Comment
    lazy val user = root userById src.user.id
    lazy val createdAt = src.created_at
    lazy val comment = src.comment
    lazy val id = src.id
    def delete = api.comments delete id
  }
  
  trait Startup {
    
    def id:String
    
    def investors:List[User]
    def founders:List[User]
    def pastInvestors:List[User]
    
    // -------- follows
    def followers           = api.follows.startup( id ).followers.users // map to users
    def doIFollow:Boolean   = api.follows.relationship( me.id, api.follows._Type.Startup, id ).source.isDefined
    def startFollowing:Unit = api.follows.startFollowing( api.follows._Type.Startup, id )
    def followsMe:Boolean   = api.follows.relationship( me.id, api.follows._Type.Startup, id ).target.isDefined
    
    // -------- comments
    
    // TODO: map to OO wrapper
    def comments = api.comments.find( api.comments._Type.Startup, id )
    
    // TODO: return comment in OO wrapper
    def comment( comment:String ){
      api.comments.create(
          api.comments._Type.Startup, id, comment)
    }
    
    def createIntro:Unit // what's the logic behind this?
    
    def jobs = api.jobs.for_startup( id )
    
    def press = api.press.forStartup( id )
    
  }
  
  trait Intro {
    protected def src: intros.Intro
    def user = userById( src.user_id )
    def startup = startupById( src.startup_id )
    def pending = src.pending
    def id = src.id
    // TODO...
  }
  
  trait Press {
    protected def src: press.Press
    lazy val owner:Option[Either[Startup, User]] = src.owner_type.toLowerCase match {
      case "startup" => startupById( src.owner_id ).map( Left( _ ) )
      case "user"    => userById( src.owner_id ).map( Right( _ ) )
    }
    lazy val url: URL = new URL( src.url )
    def id = src.id
    def postedAt  = src.posted_at
    def snippet   = src.snippet
    def title     = src.title
    def updatedAt = src.updated_at
  }
  

  trait User {
    def id:String
    def founderOf:List[Startup]
    def advisorOf:List[Startup]
    def investorOf:List[Startup]
    def pastInvestorOf:List[Startup]
    def followers = api.follows.user(id).followers.users
    def reviews = api.reviews.forUser( id )
  }
  
  trait Review {
    protected def src: reviews.Review
  }
  trait Job {

  }  

}