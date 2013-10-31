package co.angel.scala.api.simple.services.users


import java.util.Date

import co.angel.scala.api.simple.BaseService
import co.angel.scala.api.util.values._


object TagInclusion extends Enumeration {
  type TagInclusion = Value
  val none, include_parents, include_children = Value
}
import TagInclusion._

object InvestorFilter extends Enumeration {
  type InvestorFilter = Value
  val none, by_residence, by_actvity = Value
}
import InvestorFilter._




trait Service extends BaseService {
  
  val _TagInclusion = TagInclusion
  
  private val d = Map( "include_details" -> "investor" )
  
  def me():User = client.req("/me",  queryParams = d).one[User]
  
  def find( id:ALId ) =
    client.req( "/users/" + id, queryParams = d ).one[User]
  
  def batch( ids:List[ALId] ) = client.req(
      "/users/batch",
      queryParams = Map(
          "ids" -> ids.mkString(","),
          "include_details" -> "investor"
          ) ).arr[User]

  def roles( id:ALId ) =
    client.req( "/users/" + id + "/roles" ).paged[Role]("startup_roles")
    
  def find_by_slug( slug: ALSlug ):User =
    client.req( "/users/search", queryParams = Map( "slug" -> slug  ) ).one[User]
  
  def find_by_email_hash( emailMD5Hash: ALMD5Hash ):User =
    client.req( "/users/search", queryParams = Map( "md5" -> emailMD5Hash  ) ).one[User]
  
  def find_by_email( email: ALEmail ) = find_by_email_hash( email.md5Hash )
  
  def find(
    tag_id: ALId,
    include: TagInclusion = TagInclusion.none,
    investors: InvestorFilter = InvestorFilter.none
    ) = {
      import scala.collection.mutable.HashMap
      val m = new HashMap[String,String]
      include match {
        case include_parents => m += ("include_parents" -> "1")
        case include_children => m += ("include_children" -> "1")
      }
      investors match {
        case by_activity => m += ("investors" -> "by_activity")
        case by_residence => m += ("investors" -> "by_residence")
      }
      client.req(
        "/tags" + tag_id + "/users",
        queryParams = m.toMap
      ).arr[User]
  }
  
}


case class Role(
    id: String,
    role: String,
    created_at: Date,
    started_at: Date,
    ended_at: Option[Date],
    confirmed: Boolean,
    tagged: UserFromRole,
    startup: StartupFromRole
)

case class UserFromRole(
  `type` : String,
  name: String,
  id: String,
  bio: String,
  follower_count: Int,
  angellist_url: String,
  image: String
)

case class StartupFromRole(
  id:String,
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


case class User(
  id:String,
  name: String,
  bio: String,
  follower_count: Int,
  image:String,
  what_ive_built: String,  
  
  blog_url: String,
  online_bio_url: String,
  twitter_url: String,
  linkedin_url: String,
  angellist_url:String,
  aboutme_url: String,
  github_url: String,
  dribbble_url: String,
  behance_url: String,

  locations: List[Tag],
  roles: List[Tag],
  skills: Option[List[Tag]],

  investor: Boolean,
  investor_details: Option[InvestorDetails]
)

case class InvestorDetails(
  startups_per_year: String,
  average_amount: String,
  accreditation: String,
  markets: List[Tag],
  locations: List[Tag],
  investments: List[Tag]
)

case class Investment(
  id: String,
  name: String,
  quality: Int
)

case class Tag(
  id:String,
  tag_type: String,
  name: String,
  display_name: String,
  angellist_url: String
)
