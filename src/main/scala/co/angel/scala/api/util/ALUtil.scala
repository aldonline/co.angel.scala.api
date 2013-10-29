package co.angel.scala.api.util

import org.apache.http.client.utils.URIUtils
import org.apache.http.client.utils.URLEncodedUtils
import org.apache.http.message.BasicNameValuePair
import com.google.api.client.http._

object ALUtil {

  def paramsToQueryString( params:Map[String,String] ):String = {
      import scala.collection.JavaConversions._
      val x = params.map( ( t:Tuple2[String,String] ) => new BasicNameValuePair( t._1, t._2 ) )
      URLEncodedUtils.format( x.toList, "UTF-8" )
  }
  
  def paramsToURLEncodedContent( map:Map[String, String] ) = {
    val x = new UrlEncodedContent
    if ( !map.isEmpty ) x.setData( paramsToQueryString(map) )
    x
  }  
  
}