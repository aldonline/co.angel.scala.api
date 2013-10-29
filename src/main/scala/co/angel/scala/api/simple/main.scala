package co.angel.scala.api.simple

import org.json4s.DefaultFormats

import co.angel.scala.api.util.OAuthClient

class Api( token:String ){ s =>

  private lazy val client = OAuthClient( token )
  
  trait Svc extends BaseService {
    override protected def client = s.client
  }

  lazy val feed       = new services.feed.Service with Svc
  lazy val comments   = new services.comments.Service with Svc
  lazy val follows    = new services.follows.Service with Svc
  lazy val intro      = new services.intros.Service with Svc
  lazy val jobs       = new services.jobs.Service with Svc
  lazy val messages   = new services.messages.Service with Svc
  lazy val paths      = new services.paths.Service with Svc
  lazy val press      = new services.press.Service with Svc
  lazy val reviews    = new services.reviews.Service with Svc
  lazy val search     = new services.search.Service with Svc
  lazy val startups   = new services.startups.Service with Svc
  lazy val users      = new services.users.Service with Svc
  
}

trait BaseService {
  protected def client:OAuthClient
  protected implicit lazy val formats =  DefaultFormats  
}


