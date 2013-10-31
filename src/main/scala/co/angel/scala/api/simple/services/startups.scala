package co.angel.scala.api.simple.services.startups

import java.util.Date
import java.net._

import co.angel.scala.api.simple.BaseService
import co.angel.scala.api.util.values._

object Direction extends Enumeration {
  type Direction = Value
  val incoming, outgoing = Value
}
import Direction._


trait Service extends BaseService {
  
  val _Direction = Direction
  
  def find( id:ALId ) = client.req( "/startups/" + id ).one[Startup]
  
  def comments( id:ALId ) = client.req("/startups/" + id + "/comments").arr[Comment]
  
  def roles( id:ALId, direction:Direction = Direction.outgoing ) =
    client.req(
        "/startups/" + id + "/roles",
        queryParams = Map( "direction" -> direction.toString )
      ).paged[Role]("startup_roles")

   // fundraising
}

case class Role(
  id: String,
  role: String,
  started_at: Option[Date],
  ended_at: Option[Date],
  created_at: Option[Date],
  confirmed: Boolean,
  tagged: Startup, // TODO: polymorphic. Can be user or startup
  startup: Startup
)

case class Comment(
    id:String,
    comment: String,
    created_at: Date,
    user: User
)

case class User(
  name:String,
  id:String,
  bio:String,
  follower_count: Int,
  angellist_url: String,
  image: Option[String]
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
  product_desc:String,
  high_concept: String,
  follower_count: Int,
  company_url: String,
  created_at: Date,
  updated_at: Date,
  twitter_url:Option[String],
  blog_url: String,
  video_url: String,
  markets: List[Tag],
  locations: List[Tag],
  status: Status,
  screenshots: List[Screenshot]
)

case class Tag(
  id:String,
  tag_type:String,
  name:String,
  display_name:String,
  angellist_url:String
)

case class Status(
  message: String,
  created_at: Date   
)
case class Screenshot(
  thumb: String,
  original: String
)


/*
{
  "id": 6702,
  "hidden": false,
  "community_profile": false,
  "name": "AngelList",
  "angellist_url": "http://localhost:3000/angellist",
  "logo_url": "https://s3.amazonaws.com/photos.angel.co/startups/i/6702-766d1...",
  "thumb_url": "https://s3.amazonaws.com/photos.angel.co/startups/i/6702-766d1...",
  "quality": 10,
  "product_desc": "AngelList is a platform for startups to meet investors and talent.",
  "high_concept": "A platform for startups",
  "follower_count": 2849,
  "company_url": "http://angel.co",
  "created_at": "2011-03-18T00:24:29Z",
  "updated_at": "2013-01-16T03:57:59Z",
  "twitter_url": "http://twitter.com/angellist",
  "blog_url": "http://blog.angel.co",
  "video_url": "",
  "markets": [
    {
      "id": 448,
      "tag_type": "MarketTag",
      "name": "startups",
      "display_name": "Startups",
      "angellist_url": "http://angel.co/startups-1"
    },
    {
      "id": 856,
      "tag_type": "MarketTag",
      "name": "venture capital",
      "display_name": "Venture Capital",
      "angellist_url": "http://angel.co/venture-capital"
    }
  ],
  "locations": [
    {
      "id": 1692,
      "tag_type": "LocationTag",
      "name": "san francisco",
      "display_name": "San Francisco",
      "angellist_url": "http://angel.co/san-francisco"
    }
  ],
  "status": {
    "message": "You're insane if you don't follow the AngelList Twitter...",
    "created_at": "2011-08-07T00:56:25Z"
  },
  "screenshots": [
    {
      "thumb": "https://s3.amazonaws.com/.../009cff275fb96709c915c4d4-thumb.jpg",
      "original": "https://s3.amazonaws.com/.../009cff275fb96709c915c4d4-original.jpg"
    }
  ]
}
*/
