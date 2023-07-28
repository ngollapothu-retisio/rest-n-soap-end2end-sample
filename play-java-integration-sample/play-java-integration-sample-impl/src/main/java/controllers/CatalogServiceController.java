package controllers;

import akka.actor.ActorSystem;
import integration.CatalogServiceImpl;
import lombok.extern.slf4j.Slf4j;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import request.CreateCatalogRequest;
import request.PatchCatalogRequest;
import request.UpdateCatalogRequest;
import service.CatalogService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Slf4j
public class CatalogServiceController extends Controller {

    @Inject
    private CatalogService catalogService;

    public CompletionStage<Result> ping() {
        return CompletableFuture.completedFuture(ok("Ok"));
    }

    public CompletionStage<Result> getCatalogs(Http.Request request) {
        return catalogService.getCatalogs(
                request.queryString("filter"),
                request.queryString("limit"),
                request.queryString("offset")
        )
                .thenApply(r -> ok(Json.toJson(r)));
    }
    public CompletionStage<Result> getCatalog(String id) {
        return catalogService.getCatalog(id)
                .thenApply(r -> ok(Json.toJson(r)));
    }
    public CompletionStage<Result> createCatalog(Http.Request request) {
        CreateCatalogRequest createCatalogRequest = Json.fromJson(request.body().asJson(), CreateCatalogRequest.class);
        return catalogService.createCatalog(request.getHeaders(), createCatalogRequest)
                .thenApply(r -> ok(Json.toJson(r)));
    }
    public CompletionStage<Result> updateCatalog(Http.Request request){
        UpdateCatalogRequest updateCatalogRequest = Json.fromJson(request.body().asJson(), UpdateCatalogRequest.class);
        return catalogService.updateCatalog(request.getHeaders(), updateCatalogRequest)
                .thenApply(r -> ok(Json.toJson(r)));
    }

    public CompletionStage<Result> patchCatalog(Http.Request request, String id){
        PatchCatalogRequest patchCatalogRequest = Json.fromJson(request.body().asJson(), PatchCatalogRequest.class);
        return catalogService.patchCatalog(request.getHeaders(), patchCatalogRequest, id)
                .thenApply(r -> ok(Json.toJson(r)));
    }
    public CompletionStage<Result> deleteCatalog(Http.Request request, String id) {
        return catalogService.deleteCatalog(request.getHeaders(), id)
                .thenApply(r -> ok(Json.toJson(r)));
    }

}
