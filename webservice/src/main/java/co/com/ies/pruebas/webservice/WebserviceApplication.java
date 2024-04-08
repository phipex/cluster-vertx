package co.com.ies.pruebas.webservice;


import io.vertx.core.DeploymentOptions;
import io.vertx.core.ThreadingModel;
import io.vertx.core.Vertx;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class WebserviceApplication {

	private final MainVerticle mainVerticle;
	private final WorkerVerticle workerVerticle;

    public WebserviceApplication (MainVerticle mainVerticle, WorkerVerticle workerVerticle) {
        this.mainVerticle = mainVerticle;
        this.workerVerticle = workerVerticle;
    }

    public static void main(String[] args) {
		SpringApplication.run(WebserviceApplication.class, args);
	}

	@PostConstruct
	public void init(){
		startVertx();
	}

	private void startVertx() {

		ClusterManager mgr = new HazelcastClusterManager ();

		Vertx
				.builder()
				.withClusterManager(mgr)
				.buildClustered()
				.onComplete(res -> {
					Vertx vertx = null;
					if (res.succeeded()) {
						vertx = res.result();
					} else {
						// failed!
						vertx = Vertx.vertx();
					}
					vertx.deployVerticle(mainVerticle);
					DeploymentOptions options = new DeploymentOptions().setThreadingModel(ThreadingModel.WORKER);
					vertx.deployVerticle (workerVerticle,options);
				});


	}
}
