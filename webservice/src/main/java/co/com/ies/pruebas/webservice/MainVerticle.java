package co.com.ies.pruebas.webservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Component
public class MainVerticle extends AbstractVerticle {
    public static final int PORT = 8888;
    public static final String GET_ALL_ARTICLES = "request";
    private static Logger log =
            Logger.getLogger (MainVerticle.class.getPackageName ());

    private final AtomicLong counter = new AtomicLong ();
    private static final String template = "Hello Docker, %s!";



    @Override
    public void start () throws Exception {
        log.info ("MainVerticle.start");
        HttpServer server = vertx.createHttpServer ();

        server.requestHandler (request -> {
            log.info ("MainVerticle.start server.requestHandler");

            vertx.eventBus ()
                    .<String>request (GET_ALL_ARTICLES, "")
                    .onComplete (result -> {
                        System.out.println ("MainVerticle.start.onComplete");
                        if (result.succeeded ()) {

                            String body = result.result ()
                                    .body ();
                            System.out.println ("Received reply: " + body);
                            request.response ()
                                    .putHeader ("content-type", "text/json")
                                    .setStatusCode (200)
                                    .end (body);
                        } else {
                            request.response ()
                                    .setStatusCode (500)
                                    .end ();
                        }
                    });

        });

        server.listen (PORT, http -> {
            if (http.succeeded ()) {

                log.info ("HTTP server started on port " + PORT);
            } else {
                log.info ("MainVerticle.start failed " + http.cause ());

            }
        });

    }


}
