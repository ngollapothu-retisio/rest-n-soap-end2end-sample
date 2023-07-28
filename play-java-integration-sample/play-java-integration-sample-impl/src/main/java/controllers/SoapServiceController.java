package controllers;

import integration.MySoapClient;
import lombok.extern.slf4j.Slf4j;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@Slf4j
public class SoapServiceController extends Controller {

    @Inject
    private MySoapClient mySoapClient;

    public CompletionStage<Result> getCountry(String name) throws Exception {
        return mySoapClient.getCountry(name)
                .thenApply(r -> ok(Json.toJson(r)));
    }
}
