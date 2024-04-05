package co.com.ies.pruebas.webservice;

import com.sun.tools.javac.Main;
import io.vertx.core.Vertx;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class WebserviceApplication {

	private final MainVerticle mainVerticle;

    public WebserviceApplication (MainVerticle mainVerticle) {
        this.mainVerticle = mainVerticle;
    }

    public static void main(String[] args) {
		SpringApplication.run(WebserviceApplication.class, args);
	}

	@PostConstruct
	public void init(){
		Vertx vertx = Vertx.vertx ();
		vertx.deployVerticle (mainVerticle);
		vertx.deployVerticle (new MainVerticle2 ());
	}
}
