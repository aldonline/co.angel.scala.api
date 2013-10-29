package co.angel.scala.api.util

import org.apache.http.client.utils.URLEncodedUtils
import org.apache.http.message.BasicNameValuePair
import com.google.api.client.http._

object ALUtil {

  object implicits {
    implicit class FFX[X]( x:X ){ def ->>(f: X => Unit) = { f(x); x } }  
  }
  
  import implicits._
  
  def paramsToQueryString( params:Map[String,String] ):String = {
      import scala.collection.JavaConversions._
      val x = params.map {  t => new BasicNameValuePair( t._1, t._2 ) } 
      URLEncodedUtils.format( x.toList, "UTF-8" )
  }
  
  def paramsToURLEncodedContent( map:Map[String, String] ) =
    new UrlEncodedContent ->> { u => if ( map.nonEmpty ) u setData paramsToQueryString( map ) }
  
}