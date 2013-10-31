package co.angel.scala.api.simple.services.reviews

import co.angel.scala.api.util.values._
import java.util.Date

import co.angel.scala.api.simple.BaseService
import scala.collection.SeqView

trait Service extends BaseService {

  private def m( user_id: Option[ALId] ) = {
    val queryParams = user_id match {
      case None => Map.empty[String,String]
      case Some(id) => Map( "user" -> id.str )
    }
    // 1. first request to get total positives ( yes, we could avoid this query )
    val total_pos = client.req( "/reviews", queryParams = queryParams ).one[TotalPositive].total_positive
    // 2. build pager
    val pager = client.req( "/reviews", queryParams = queryParams ).paged[Review]("reviews")
    // 3. combine
    new ReviewReport( total_pos, pager ) 
  }
  
  def forMe( ) = m( None )
  def forUser( id:ALId ) = m( Some(id) )
}

class ReviewReport(
  val total_positive:Int,
  val reviews:SeqView[Review, Seq[_] ]
)

case class TotalPositive( total_positive: Int )

case class Review(
  id:String,
  rating: Int,
  note: String,
  created_at: Date,
  relationship_to_reviewer: Rel
)

case class Rel(
  as: String,
  relationship: String
)


/*
 
{
  "total_positive": 2,
  "total": 3,
  "per_page": 50,
  "page": 1,
  "last_page": 1,
  "reviews": [
    {
      "id": 1098,
      "rating": 1,
      "note": "Naval is hard working, honest and he really cares...",
      "created_at": "2011-08-12T02:49:30Z",
      "relationship_to_reviewer": {
        "as": "investor",
        "relationship": "investor"
      }
    },
    {
      "id": 404,
      "rating": 1,
      "note": "Very entrepreneur friendly.  Super sharp.",
      "created_at": "2011-07-25T22:23:41Z",
      "relationship_to_reviewer": {
        "as": "founder",
        "relationship": "investor"
      }
    },
    {
      "id": 12,
      "rating": -1,
      "created_at": "2011-07-22T02:12:50Z"
    }
  ]
}
 
 */