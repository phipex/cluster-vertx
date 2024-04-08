package co.com.ies.pruebas.webservice;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import org.springframework.stereotype.Component;

@Component
public class WorkerVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        MessageConsumer<String> consumer = vertx.eventBus ().consumer(MainVerticle.GET_ALL_ARTICLES);
        consumer.handler(message -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            System.out.println("I have received a message: " + message.body());
            message.reply("how interesting!");
        });
    }
}
