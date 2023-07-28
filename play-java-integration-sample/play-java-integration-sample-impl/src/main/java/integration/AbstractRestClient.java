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
public abstract class AbstractRestClient {

    public final CircuitBreaker breaker;

    private final WSClient ws;
    private final ActorSystem classicActorSystem;
    private final ExecutionContextExecutor executionContextExecutor;
    private final Config serviceConfig;
    private final String url;

    protected AbstractRestClient(ActorSystem classicActorSystem, WSClient ws, ExecutionContextExecutor executionContextExecutor, String service){
        this.classicActorSystem = classicActorSystem;
        this.ws = ws;
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
        this.url = serviceConfig.getString("scheme") +"://"+serviceConfig.getString("host")
                + Optional.ofNullable(serviceConfig.getString("port")).map(p -> ":"+p).orElseGet(()->"");
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

    public WSRequest service(String uri){
        return ws.url(this.url+Optional.ofNullable(uri).orElseGet(()->""));
    }
    public String urlEncode(String value){
        try {
            return java.net.URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static ObjectMapper objectMapper = new ObjectMapper();
    public <T> T getObject(String jsonString,  Class<T> valueType) {
        if(jsonString == null){
            return null;
        }
        try {
            return objectMapper.readValue(jsonString, valueType);
        } catch (JsonProcessingException e) {
            log.error("error::", e);
        }
        return null;
    }
    public static String toJsonString(Object object) {
        if(object == null){
            return null;
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("error::", e);
        }
        return null;
    }
}
