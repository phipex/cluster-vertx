package co.com.ies.pruebas.webservice;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;

@Component
public class MainVerticle extends AbstractVerticle {
    public static final int PORT = 8888;
    public static final String GET_ALL_ARTICLES = "request";
    private final Logger log = LoggerFactory.getLogger(MainVerticle.class);

    private final AtomicLong counter = new AtomicLong ();
    private static final String template = "Hello Docker, %s!";



    @Override
    public void start () throws Exception {
        log.info ("MainVerticle.start");
        HttpServer server = vertx.createHttpServer ();

        server.requestHandler (request -> {
            log.debug ("MainVerticle.start server.requestHandler");

            vertx.eventBus ()
                    .<String>request (GET_ALL_ARTICLES, "")
                    .onFailure (exception -> log.error ("MainVerticle.start.onFailure", exception))
                    .onComplete (result -> {
                        log.debug ("MainVerticle.start.onComplete");
                        if (result.succeeded ()) {

                            String body = result.result ()
                                    .body ();
                            log.debug ("Received reply: " + body);
                            request.response ()
                                    .putHeader ("content-type", "text/json")
                                    .setStatusCode (200)
                                    .end (body);
                        } else {
                            log.error ("result no success",result.cause ());
                            request.response ()
                                    .setStatusCode (500)
                                    .end ();
                        }
                    });

        });

        server.exceptionHandler (exception -> log.error ("MainVerticle.start.exceptionHandler", exception));

        server.listen (PORT, http -> {
            if (http.succeeded ()) {

                log.info ("HTTP server started on port " + PORT);
            } else {
                log.info ("MainVerticle.start failed " + http.cause ());

            }
        });

    }


}
