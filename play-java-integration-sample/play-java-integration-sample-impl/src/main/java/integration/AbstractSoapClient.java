package integration;

import akka.actor.ActorSystem;
import akka.pattern.CircuitBreaker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import scala.concurrent.ExecutionContextExecutor;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.Optional;

@Slf4j
public abstract class AbstractSoapClient {

    public final CircuitBreaker breaker;

    private final ActorSystem classicActorSystem;
    private final ExecutionContextExecutor executionContextExecutor;
    private final Config serviceConfig;

    protected AbstractSoapClient(ActorSystem classicActorSystem, ExecutionContextExecutor executionContextExecutor, String service){
        this.classicActorSystem = classicActorSystem;
        this.executionContextExecutor = executionContextExecutor;
        this.serviceConfig = classicActorSystem.settings().config()
                .getConfig("integration.service."+service);
        Config circuitBreakerConfig = serviceConfig.getConfig("circuit-breaker");
        this.breaker = new CircuitBreaker(
                executionContextExecutor,
                classicActorSystem.getScheduler(),
                circuitBreakerConfig.getInt("max-failures"),
                Duration.ofSeconds(circuitBreakerConfig.getInt("call-timeout")),
                Duration.ofSeconds(circuitBreakerConfig.getInt("reset-timeout")))
                .addOnCloseListener(this::onClose)
                .addOnHalfOpenListener(this::onHalfOpen)
                .addOnOpenListener(this::onOpen);
    }

    private void onHalfOpen() {
        log.warn("onHalfOpen, CircuitBreaker is now half open");
    }

    private void onClose() {
        log.info("onClose, CircuitBreaker is now close");
    }

    private void onOpen() {
        log.warn("onOpen, CircuitBreaker is now open");
    }

    public String endpoint(){
        return serviceConfig.getString("endpoint");
    }

}
