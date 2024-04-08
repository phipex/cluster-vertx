package co.com.ies.pruebas.webservice;

import com.sun.tools.javac.Main;
import io.vertx.core.Vertx;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class WebserviceApplication {

	private final MainVerticle mainVerticle;
	private final MainVerticle2 mainVerticle2;
	private final AppServer appServerVerticle;

    public WebserviceApplication (MainVerticle mainVerticle, MainVerticle2 mainVerticle2, AppServer appServerVerticle) {
        this.mainVerticle = mainVerticle;
        this.mainVerticle2 = mainVerticle2;
        this.appServerVerticle = appServerVerticle;
    }

    public static void main(String[] args) {
		SpringApplication.run(WebserviceApplication.class, args);
	}

	@PostConstruct
	public void init(){
		startVertx();
	}

	private void startVertx() {
		Vertx vertx = Vertx.vertx();

		vertx.deployVerticle(appServerVerticle);
		vertx.deployVerticle(mainVerticle);
		vertx.deployVerticle(mainVerticle2);
	}
}
