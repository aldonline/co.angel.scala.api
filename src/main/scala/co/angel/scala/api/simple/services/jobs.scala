package co.angel.scala.api.simple.services.jobs

import java.util.Date

import co.angel.scala.api.simple.BaseService

trait Service extends BaseService {
  def all = client.req( "/jobs" ).paged[Job]("jobs")
  
  def get( id:String ) = client.req( "/jobs/" + id ).one[Job]
  
  def for_location_tag( location_tag_id:String ) =
    client.req( "/tags/" + location_tag_id + "/jobs" ).paged[Job]("jobs")
  
  def for_startup( startup_id:String ) =
    client.req("/startups/" + startup_id + "/jobs").arr[Job]
}

case class Startup(
  id :String,
  hidden: Boolean,
  community_profile: Option[Boolean],
  name: String,
  angellist_url: String,
  logo_url: String,
  thumb_url: String,
  product_desc: String,
  high_concept: String,
  follower_count: Int,
  company_url: String
)

case class Tag(
  id:String,
  tag_type:String,
  name:String,
  display_name: String,
  angellist_url: String
)

case class Job(
  id: String,
  title: String,
  
  created_at: Date,
  updated_at: Date,
  
  equity_cliff: Option[Float],
  equity_min: Option[Float],
  equity_max: Option[Float],
  salary_min: Option[Int],
  salary_max: Option[Int],
  
  angellist_url: String,
  startup: Option[ Startup ],
  
  tags: List[Tag]
)

