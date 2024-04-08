package co.com.ies.pruebas.webservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class WorkerVerticle extends AbstractVerticle {

    private static final String template = "Hello Docker, %s!";
    private final AtomicLong counter = new AtomicLong();

    ObjectMapper om = new ObjectMapper().registerModule(new JavaTimeModule ());
    @Override
    public void start() throws Exception {
        MessageConsumer<String> consumer = vertx.eventBus ().consumer(MainVerticle.GET_ALL_ARTICLES);
        consumer.handler(message -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

            String format = String.format(template, "World");
            String ip = "No found";

            // Local address
            try {
                String hostAddress = InetAddress.getLocalHost().getHostAddress() ;
                String hostName = InetAddress.getLocalHost().getHostName();
                ip = hostAddress;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            System.out.println("I have received a message: " + message.body());
            message.reply(encode(new Greeting(counter.incrementAndGet(),
                    format, ip)));
        });
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
