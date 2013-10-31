package co.angel.scala.api.simple.services.paths

import java.util.Date

import org.json4s._
import org.json4s.jvalue2extractable

import co.angel.scala.api.simple.BaseService
import co.angel.scala.api.util.values._

object Direction extends Enumeration {
  type Direction = Value
  val following, followed = Value
}
import Direction._


trait Service extends BaseService {

  val _Direction = Direction
  
  def users( ids:Seq[ALId], direction:Direction ) = m( ids, "user_ids", direction )
  def startups( ids:Seq[ALId], direction:Direction ) = m( ids, "startup_ids", direction )
  
  private def m( ids:Seq[ALId], prop:String, direction:Direction ):Map[String,List[Path]] = {
    val x = client.req(
        "/paths",
        queryParams = Map(
            prop -> ids.map(_.str).mkString(","),
            "direction" -> direction.toString
        )).oneJson
    val keys = x.values.asInstanceOf[Map[String,_]].keys
    val res = keys map { key => key ->  ( x \ key ).extract[List[List[Path]]].head }
    Map( res.toSeq:_* )
  }
}

case class Path(
  connector: Agent,
  connection: Connection )

case class Connection(
  `type`: String,
  via: Option[Agent],
  in:  Option[String],
  out: Option[String] )

case class Agent(
  `type`: String,
  id: String,
  name: String,
  image: String,
  angellist_url: String )






/*
 
{
  "2": [
    [
      {
        "connector": {
          "id": 1941,
          "type": "User",
          "name": "Milos Tatarevic",
          "image": "https://s3.amazonaws.com/photos.angel.co/users/1941-medium?1306894182",
          "angellist_url": "http://angel.co/milos"
        },
        "connection": {
          "type": "explicit",
          "via": null,
          "in": null,
          "out": null
        }
      },
      {
        "connector": {
          "id": 276,
          "type": "User",
          "name": "Brendan Baker",
          "image": "https://s3.amazonaws.com/photos.angel.co/users/276-medium?1305680272",
          "angellist_url": "http://angel.co/brendan"
        },
        "connection": {
          "type": "startup_role",
          "via": {
            "id": 6702,
            "type": "Startup",
            "name": "AngelList",
            "image": "https://s3.amazonaws.com/photos.angel.co/startups/i/6702-766d1ce00c99ce9a5cbc19d0c87a436e-thumb.",
            "angellist_url": "http://angel.co/angellist"
          },
          "in": "employee",
          "out": "founder"
        }
      },
      {
        "connector": {
          "id": 2,
          "type": "User",
          "name": "Babak Nivi",
          "image": "https://s3.amazonaws.com/photos.angel.co/users/2-medium?1307554953",
          "angellist_url": "http://angel.co/nivi"
        },
        "connection": {
          "type": null,
          "via": null,
          "in": null,
          "out": null
        }
      }
    ],
    [
      {
        "connector": {
          "id": 1941,
          "more data here": "..."
        }
      }
    ]
  ],
  "155": [
    [
      {
        "paths to the user with id=155": "..."
      }
    ]
  ]
} 
  
 * */

