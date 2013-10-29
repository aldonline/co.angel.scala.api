package co.angel.scala.api.util

import org.json4s._
import scala.collection.mutable.SynchronizedMap
import scala.collection.SeqView

object AngelListPager {
  def apply( fetchPage: Int => JValue, propName:String  ) =
    new AngelListPager( fetchPage, propName ).toSeq
}


private class AngelListPager( fetchPage: Int => JValue, propName:String ) {
  
  private implicit lazy val formats = DefaultFormats
  
  // all pages have the total, per_page, etc values
  // we pick the first one
  lazy val total: Int = fetchPageCached( 0 ).total // TODO: Perf. we should pick whichever is already loaded
  
  private lazy val per_page: Int = fetchPageCached( 0 ).per_page
  
  def getItem( index: Int ): JValue = {
    if ( index >= total ) throw new IndexOutOfBoundsException
    val pageNum = Math.floor( index / per_page ).toInt
    val indexInPage = index % per_page
    fetchPageCached( pageNum ).getItem( indexInPage )
  }

  private class Page( index:Int, jsv:JValue ){
    private lazy val itemsArr = (jsv \ propName ).asInstanceOf[JArray]
    lazy val per_page         = (jsv \ "per_page").extract[Int]
    lazy val total            = (jsv \ "total").extract[Int]   
    def getItem( index: Int ): JValue = itemsArr( index )
  }
  
  private lazy val cache =
    new scala.collection.mutable.HashMap[Int, Page]
      with SynchronizedMap[Int, Page] // shared state
  
  private def fetchPageCached( index:Int ):Page =
    cache.getOrElseUpdate( index, new Page( index, fetchPage(index) ) )
  
  // this is where the magic happens
  // we wrap everything in an IndexedSeq interface
  // but return its lazy ( non-strict ) view
  lazy val toSeq =
    new IndexedSeq[JValue]{
      override def length:Int = total
      override def apply( index:Int ) = getItem( index )
    }.view
    
}