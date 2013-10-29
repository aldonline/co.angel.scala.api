# Overview

I built this during an afternoon to force me to understand the data
model behind the [Angel List API](http://angel.co/api). 

IT IS NOT COMPLETE
( Probably half way there )

Since Scala is not a popular language amongst startups I will probably
leave this up to here and eventually keep on going on-demand.
If you actually need this let me know. If you are sufficiently convincing
you may get me to finish it.

The basic strategy is as follows:

1. Create a thin typesafe wrapper atop the REST+JSON services exposed by the API
2. Create an OO layer on top

The simple wrapper is located in the `co.angel.scala.api.simple.*` package.
The OO layer is in the `co.angel.scala.api.oo.*` package.

The services layer is half way there and it shouldn't be hard to finish.
It is organized following the structure on the [Api Documentation Website](https://angel.co/api).

# Disclaimer

* This library is NOT ENDORSED or mantained by Angel List. It's a personal hack.
* It is FAR from production ready. Don't be stupid. Really.
* The services layer uses a denormalized approach to DTOs/Case Classes. It is pretty ugly
  and certainly not idiomatic. But The idea is to wrap each service
  atomically by capturing its semantics and nuances as close to the source as possible.
  Integration/Normalization can be built above this typesafe layer.
* If you start building algebraic expressions with the collections API you will most certainly
  break things. ( TODO: We should add throttling to the low level client eventually. )

# Setup

Not sure if this works ( my machine has Scala and other stuff already installed so I can't test this.
  Please try, learn, and then Pull request as necessary ).

Naive attempt:

```shell

# Get the latest Java
# ( Yeah. It sucks. But think about this: You will be one step closer to using Clojure as well )

# install the Scala Build Tool 
# http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html
brew install sbt

# grab the code
git clone https://github.com/aldonline/co.angel.scala.api.git
cd co.angel.scala.api

# start the console
# Note: if this thing worked, which would mean that SBT a lot smarter than I thought
# this step will download half of the internet.
sbt console

# and now you should be ready to try the examples below

```

# Examples

```scala

val api = new co.angel.scala.api.simple.Api( "insert your token here" )
// you can get a 'bearer' oauth2 token here: https://angel.co/api/oauth/clients


// the description of the first 10 entries in the personalized
// feed of the current user
api.feed.personalized.take(10).map( _.description ).mkString("\n")

// note that all collections are lazy / non strict
val entries = api.feed.personalized

// getting the length will fetch only the first page
// the length is included on each page
println( entries.length ) // --> 150

  
// We map over the description accessor to create
// a new list. this will NOT go the the server
val descriptions = entries.map( _.description )

// in fact we can keep on mapping
val descriptionsUC = descriptions.map( _.toUpperCase)

// ok let's get the first 10 values and print them
descriptionsUC.take( 10 ).mkString

```

# More examples

```scala

// the number of followers of user "671"
api.follows.user( "671" ).followers.users.ids().length

// who is user 671?
api.users.user("671").name

// jobs for startup 6702
api.jobs.for_startup("6702").map( _.title ).mkString("\n")

// startup 6702?
api.startups.startup("6702")

// and so on...

```

The following example hits the `/feed` service, filters our one specific kind of entry,
and then hits the `/jobs` service. Not very performant, but it makes a for a nice example.

```scala

  val xs = for {
    entry <- api.feed.personalized               // get feed entries
    if ( entry.item.`type` == "JobListing" )     // retain only "JobListing"s
    id <- entry.item.ids                         // get the associated IDs
    job = api.jobs.get( id )                     // for each ID hit the /jobs service
    startup <- job.startup                       // and extract the startup associated with each job
  } yield {
      // with the data we extracted, lets yield an HTML snippet
      <div>
        <h2>{job.title} @ {startup.name}</h2>
        <div>Salary from
            <span>{job.salary_min.getOrElse("")}</span>
             to
            <span>{job.salary_max.getOrElse("")}</span>
        </div>
     </div>                                        
   }
  
   // now lets put every entry inside an unodered list ( `ul > li` )
   val res =
     <ul>
      {xs.take(3).map( n =>
        <li>
          { n.asInstanceOf[scala.xml.Node] }
        </li> ) }
     </ul>


```

