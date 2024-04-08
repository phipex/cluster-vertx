package co.com.ies.pruebas.webservice;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
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
    private static Logger log =
            Logger.getLogger(MainVerticle.class.getPackageName());

    private final AtomicLong counter = new AtomicLong();
    private static final String template = "Hello Docker, %s!";

    @Override
    public void start() throws Exception {
        System.out.println ("MainVerticle.start");
        log.info("MainVerticle.start");
        HttpServer server = vertx.createHttpServer();

        server.requestHandler(request -> {
            log.info("MainVerticle.start server.requestHandler");
            // This handler gets called for each request that arrives on the server
            HttpServerResponse response = request.response();
            //response.putHeader("content-type", "text/plain");

            // Write to the response and end it
            response.end("Hello World!");
        });

        server.listen(PORT, http -> {
            if (http.succeeded()) {

                log.info("HTTP server started on port "+PORT);
            } else {
                log.info("MainVerticle.start failed " + http.cause());

            }
        });


/*        Router router = createRouter();



        vertx.createHttpServer()
               // .requestHandler(router)
                .requestHandler(req -> req.response()
                        .putHeader("content-type", "text/plain")
                        .end("Hello from Vert.x!"))

                .listen(PORT, http -> {
            if (http.succeeded()) {

                System.out.println("HTTP server started on port "+PORT);
            } else {
                System.out.println ("MainVerticle.start failed " + http.cause());

            }
        });*/
    }

    private Router createRouter(){
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.get("/").handler(this::helloHandler);
        router.get("/greeting").handler(this::helloHandler);
        return router;
    }

    private void helloHandler(RoutingContext context){
        log.info("ACCESS 200 [/]");
        context.response()
                .putHeader("content-type", "text/plain")
                .end("Hello from Vert.x!");
    }

    private void greetingHandler(RoutingContext context){
        log.info("ACCESS 200 [/]");
        vertx.eventBus().request("barking","test",handler->{
            if(handler.succeeded()){
                log.info("ACCESS 200 [/barking]");
                context.response()
                        .end(handler.result().body().toString());
            } else{
                log.info("ACCESS 500 [/barking]");
                context.response().setStatusCode(500)
                        .end(handler.cause().getMessage());
            }
        });
    }

    private Greeting getNew(){
        String name = "Hello from Vert.x";
        String format = String.format(template, name);
        String ip = "No found";

        // Local address
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress() ;
            String hostName = InetAddress.getLocalHost().getHostName();
            ip = hostAddress;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return new Greeting(counter.incrementAndGet(),
                format, ip);
    }
}
