package integration;

import akka.actor.ActorSystem;
import com.example.ws.client.CountriesPortServiceStub;
import com.example.ws.client.CountriesPortServiceStub.Country;
import com.example.ws.client.CountriesPortServiceStub.GetCountryRequest;
import com.example.ws.client.CountriesPortServiceStub.GetCountryResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.axis2.AxisFault;
import scala.concurrent.ExecutionContextExecutor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.rmi.RemoteException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
@Slf4j
public class MySoapClient extends AbstractSoapClient {

    private CountriesPortServiceStub countriesPortService;

    @Inject
    protected MySoapClient(ActorSystem classicActorSystem, ExecutionContextExecutor executionContextExecutor, String service) throws AxisFault {
        super(classicActorSystem, executionContextExecutor, "mysoap.example");
        countriesPortService = new CountriesPortServiceStub(endpoint());
    }

    public CompletionStage<Country> getCountry(String name) throws Exception {
        return breaker.callWithCircuitBreakerCS(()->{
            GetCountryRequest getCountryRequest = new GetCountryRequest();
            getCountryRequest.setName(name);
            return CompletableFuture.supplyAsync(()->{
                try {
                    GetCountryResponse getCountryResponse = countriesPortService.getCountry(getCountryRequest);
                    log.info("getCountryResponse:: {}", getCountryResponse.getCountry());
                    return getCountryResponse.getCountry();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                return new Country();
            });
        });


    }
}
