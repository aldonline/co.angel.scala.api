package co.angel.scala.api.util

import java.io.StringWriter
import org.apache.commons.io.IOUtils
import org.json4s.native.JsonMethods.parse
import com.google.api.client.auth.oauth2._
import com.google.api.client.http._
import com.google.api.client.http.javanet.NetHttpTransport
import org.apache.http.client.entity.UrlEncodedFormEntity
import scala.collection.JavaConversions._
import org.json4s._

/*

TODO: handle other http status codes
TODO: gzip

*/


class HttpMethod

class OAuthClient( private val bearerToken:String ) { s =>
    
  private implicit lazy val formats =  DefaultFormats
  // some implicits to make our lifes easier
  private implicit def str2genericURL( str:String ) = new GenericUrl( str )
  
  
  private lazy val rf =
    new NetHttpTransport createRequestFactory
      new Credential( BearerToken.authorizationHeaderAccessMethod ).setAccessToken(bearerToken)
  
  private def resolveURL( url:String, queryParams: Map[String,String] = Map.empty ) = {
    
    val u = if ( url startsWith "https://" ) url else "https://api.angel.co/1" + url  
    
    // add query params to URL if needed
    val qs = if ( ! queryParams.isEmpty )
        "?" + ALUtil.paramsToQueryString(queryParams)
      else ""
    u + qs
  
  }
  
  val GET    = new HttpMethod()
  val POST   = new HttpMethod()
  val DELETE = new HttpMethod()
  
  def req(
    url:            String,
    queryParams:    Map[String, String]  = Map.empty,
    method:         HttpMethod           = GET,
    bodyParams:     Map[String,String]   = Map.empty 
  ) = {
    
    if ( method == GET && bodyParams.nonEmpty )
      throw new IllegalArgumentException("You cannot pass body parameters to a GET request")    
    
    val url2 = resolveURL( url, queryParams )
    
    val request = method match {
      case GET    => rf buildGetRequest    url2
      case DELETE => rf buildDeleteRequest url2
      case POST   => rf.buildPostRequest(  url2, ALUtil paramsToURLEncodedContent bodyParams )
    }
    
    def exec:JValue = requestToJson( request )
    
    new Object {
      
      def one[T]( implicit mf:Manifest[T] ):T = oneJson.extract[T]
      def oneJson:JValue = exec
      
      def arr[T]( implicit mf:Manifest[T] ):List[T] = arrJson.map( _.extract[T] )
      def arrJson:List[JValue] = exec.asInstanceOf[JArray].children.toList          
      
      def paged[T]( propName:String )( implicit mf:Manifest[T] ) =
        pagedJson( propName ) map {  _.extract[T] }      
      
      // No extraction to case classes
      def pagedJson( propName:String ) = {
        
        // special case. we basically use a completely different logic here
        
        if ( method != GET ) throw new IllegalArgumentException("You can only page over a GET request")
        
        def pageToJson:( Int=>JValue ) = (i:Int) =>
          requestToJson( rf buildGetRequest resolveURL( url, queryParams + ( "page" -> i.toString ) ) )
        
        AngelListPager( pageToJson, propName )
        
      }
    }
  }

  private def requestToJson( req:HttpRequest ) = parse( requestToString( req ) )
  
  private def requestToString( req:HttpRequest ):String = {
    val writer = new StringWriter
    IOUtils.copy( req.execute.getContent, writer, "UTF8" )
    writer.toString
  }
  
}

object OAuthClient {
  def apply( bearerToken:String ) = new OAuthClient( bearerToken )
}