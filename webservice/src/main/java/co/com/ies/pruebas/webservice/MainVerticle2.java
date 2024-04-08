package co.com.ies.pruebas.webservice;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.springframework.stereotype.Component;

@Component
public class MainVerticle2 extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        System.out.println ("MainVerticle2.start");
        vertx.createHttpServer().requestHandler(req -> {
            System.out.println ("MainVerticle2.start");
            req.response()
                    //.putHeader("content-type", "text/plain")
                    .end("Hello from Vert.x!");
        }).listen(1234, http -> {
            if (http.succeeded()) {
                startPromise.complete();
                System.out.println("HTTP server started on port 1234");
            } else {
                startPromise.fail(http.cause());
            }
        });
    }
}
