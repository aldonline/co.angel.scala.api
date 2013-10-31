package co.angel.scala.api.util

import org.apache.commons.codec.digest.DigestUtils

object values {

  trait ALStringValue {
    def str:String
    override def toString = str
  }
  
  case class ALId( str:String ) extends ALStringValue
  case class ALSlug( str:String ) extends ALStringValue
  case class ALEmail( str:String ) extends ALStringValue {
    lazy val md5Hash = ALMD5Hash( DigestUtils.md5Hex( str ) )
  }
  case class ALMD5Hash( str:String ) extends ALStringValue
  case class ALBearerToken( str:String ) extends ALStringValue 
  
  
  implicit def angellistvalue2str( v:ALStringValue ):String = v.str 
  
  implicit def toALBearerToken( str:String ) = ALBearerToken( str )
  implicit def toALId( str:String ) = ALId( str )
  implicit def toALSlug( str:String ) = ALSlug( str )
  implicit def toALMD5Hash( str:String ) = ALMD5Hash( str )
  implicit def toALALEmail( str:String ) = ALEmail( str )
  implicit def EmailToMD5Hash( e:ALEmail ):ALMD5Hash = e.md5Hash
  
}