package co.com.ies.pruebas.webservice;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.JWTAuthHandler;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AppServer extends AbstractVerticle {

    public static final String KEYBOARD_CAT = "keyboard cat";



    ObjectMapper om = new ObjectMapper().registerModule(new JavaTimeModule());



    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);

        // Create a JWT Auth Provider
        JWTAuth jwt = JWTAuth.create(
                vertx,
                new JWTAuthOptions().addPubSecKey(new PubSecKeyOptions().setAlgorithm("HS256").setBuffer(KEYBOARD_CAT))
        );

        router
                .post("/api/v1/authenticate/external")
                .handler(rtx -> {
                    System.out.println("rtx = " + rtx);
                    rtx
                            .request()
                            .body()
                            .onComplete(hbuffer -> {
                                if (hbuffer.succeeded()) {
                                    JsonObject body = hbuffer.result().toJsonObject();
                                    String username = body.getString("username");
                                    String password = body.getString("password");

                                    //todo validar el poassword codificado
                                    System.out.println("username = " + username);
                                    System.out.println("password = " + password);

                                    if (isNotUserValid(username)) {
                                        rtx.response().setStatusCode(403).end();
                                        return;
                                    }

                                    rtx.response().putHeader("Content-Type", "text/json");
                                    String token = jwt.generateToken(
                                            new JsonObject().put("username", username),
                                            new JWTOptions().setExpiresInSeconds(getExpires())
                                    );

                                    rtx.response().end(new JsonObject().put("id_token", token).encode());
                                } else {
                                    rtx.response().setStatusCode(403).end();
                                }
                            });
                });

        // protect the API
        router.route("/api/*").handler(JWTAuthHandler.create(jwt));

        // this is the secret API
        router
                .get("/api/protected")
                .handler(ctx -> {
                    User user = ctx.user();
                    System.out.println("user = " + user);
                    System.out.println("user.subject () = " + user.subject());
                    System.out.println("user.attributes () = " + user.attributes());
                    System.out.println("user.principal () = " + user.principal());

                    ctx.response().putHeader("Content-Type", "text/plain");
                    ctx.response().end("a secret you should keep for yourself...");
                });

        router
                .get("/api/v1/program-member/total-points")
                .handler(ctx -> {
                    User user = ctx.user();
                    System.out.println("user = " + user);


                    ctx.response().putHeader("Content-Type", "text/json");
                    ctx.response().end("{}");
                });

        router
                .get("/api/v1/program-member/details-points")
                .handler(ctx -> {
                    User user = ctx.user();
                    System.out.println("user = " + user);


                    ctx.response().putHeader("Content-Type", "text/json");
                    ctx.response().end("{}");
                });

        router
                .get("/api/v1/program-member/customer-detail")
                .handler(ctx -> {
                    User user = ctx.user();
                    System.out.println("user = " + user);

                      //JsonObject jsonObject = new JsonObject(encode(clientRecord));
                    //System.out.println("jsonObject = " + jsonObject);
                    ctx.response().putHeader("Content-Type", "text/json");
                    ctx.response().end("{}");
                });

        router
                .get("/api/v1/program-member/details-points/service")
                .handler(ctx -> {
                    User user = ctx.user();
                    System.out.println("user = " + user);

                    ctx.response().putHeader("Content-Type", "text/json");
                    ctx.response().end("{}");
                });

        router
                .get("/api/v1/program-member/details-points/service/:service")
                .handler(ctx -> {
                    User user = ctx.user();
                    System.out.println("user = " + user);
                    String service = ctx.pathParam("service");


                    ctx.response().putHeader("Content-Type", "text/json");
                    ctx.response().end("{}");
                });

        vertx.createHttpServer().requestHandler(router).listen(8282);
    }

    private static int getExpires() {
        return 60 * 10;
    }

    private boolean isNotUserValid(String username) {
        return false;
    }

    private <T> String encode(T object) {
        try {
            return om.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }
}

