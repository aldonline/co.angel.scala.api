package co.angel.scala.api.simple.services.intros

import java.util.Date
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._
import co.angel.scala.api.util._
import co.angel.scala.api.simple._

trait Service extends BaseService {
  def create( startup_id:String, note:String ): Intro =
    client.req(
      url = "/intros",
      bodyParams = Map("startup_id" -> startup_id, "note" -> note),
      method = client.POST  
      ).one[Intro]
}

case class Intro (
  id : String,
  startup_id: String,
  user_id: String,
  created_at: Date,
  intro_count: Int,
  pending: Boolean,
  note: String
)