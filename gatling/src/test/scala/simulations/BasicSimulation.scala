package scala.simulations

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class BasicSimulation extends Simulation {

    val isDocker = true

    val urlBase = if (isDocker) "http://webservice.docker.localhost" else "http://localhost:8081"

    val httpProtocol = http.baseUrl(urlBase).acceptHeader("application/json, text/plain, */*")

    val scn = scenario("BasicSimulation")
        .exec(
        http("Request")
            .get("/greeting")
            .check(status.in(200 to 210))
        )
        .exitHereIfFailed

  setUp(
    //scn.inject(atOnceUsers(100))
    scn.inject(rampUsers(1000).during(60.seconds))
  ).protocols(httpProtocol)


}