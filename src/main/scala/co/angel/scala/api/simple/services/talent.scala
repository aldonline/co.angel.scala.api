package co.angel.scala.api.simple.services.talent

import java.util.Date

import co.angel.scala.api.simple.BaseService

trait Service extends BaseService {

  def startups =
    client.req( "/talent/startups" ).one[StartupsResult]
  
  def candidates( startup_id: String ) =
    client.req(
        "/talent/candidates",
        queryParams = Map( "startup_id" -> startup_id )
    ).one[CandidatesResult]
  
  
/*
startup_id
required
The id of the company in the pairing.
user_id
optional
The id of the user in the pairing. If no user_id is passed, the current user's id is used.
startup_note
optional
An optional note from the company to the candidate.
startup_interested
optional
1 if the company is interested in the candidate, 0 if the company is uninterested.
user_note
optional
An optional note from the candidate to the company.
user_interested
optional
1 if the candidate is interested in the company, 0 if the candidate is uninterested. 
*/  
  
  // TODO: what are the valid combinations?
  // so we can create strongly typed wrappers
  
  /*
  def createPairing(
      startup_id:String,
      user_id:Option[String],
      startup_note:Option[String],
      startup_interested:Option[Boolean],
      user_note: Option[String],
      user_interested: Option[Boolean]
    ) = {
      client.req(
        "/talent/pairing",
        method = client.POST,
        bodyParams = Map(
          "startup_id"    
        )
          
      ).one[PairingResult]
      
      */
}

case class PairingResult(
    
)

case class StartupsResult(
  yes: List[Startup],
  no: List[Startup],
  matched: List[Startup],
  interested: List[Startup],
  all: List[Startup]
)


case class Startup(    
  // TODO
)

case class User()

case class CandidatesResult(
  yes: List[User],
  no: List[User],
  matched: List[User],
  interested: List[User],
  all: List[User]
)




