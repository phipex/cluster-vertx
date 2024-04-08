package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class BasicSimulationVertx extends Simulation {

    val isDocker = false

    val urlBase = if (isDocker) "http://webservice2.docker.localhost" else "http://localhost:8888"

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