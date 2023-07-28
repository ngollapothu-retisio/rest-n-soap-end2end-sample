package integration;

import akka.actor.ActorSystem;
import lombok.extern.slf4j.Slf4j;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.mvc.Http;
import request.CreateCatalogRequest;
import request.PatchCatalogRequest;
import request.UpdateCatalogRequest;
import response.GetCatalogResponse;
import response.GetCatalogsResponse;
import service.CatalogService;
import util.MyExecutionContext;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletionStage;

@Slf4j
public class CatalogServiceImpl extends AbstractRestClient implements CatalogService {

    @Inject
    public CatalogServiceImpl(ActorSystem classicActorSystem, WSClient ws, MyExecutionContext executionContextExecutor) {
        super(classicActorSystem, ws, executionContextExecutor, "catalog");
    }

    @Override // GET /catalogs
    public CompletionStage<GetCatalogsResponse> getCatalogs(Optional<String> filter, Optional<String> limit, Optional<String> offset) {
        return breaker.callWithCircuitBreakerCS(()->{
                    return service("/catalogs")
                            .addHeader("Authorization", "Test + Token1")
                            .setQueryString(
                                    new HashMap<String, List<String>>() {{
                                        filter.ifPresent(p -> {
                                            put("filter", Arrays.asList(p));
                                        });
                                        limit.ifPresent(p -> {
                                            put("limit", Arrays.asList(p));
                                        });
                                        offset.ifPresent(p -> {
                                            put("offset", Arrays.asList(p));
                                        });
                                    }}
                            )
                            .get();
                })
                .handle((wsResponse, e) -> {
                    if(Objects.nonNull(e)) {
                        throw new RuntimeException(e);
                    }
                    return getObject(wsResponse.getBody(), GetCatalogsResponse.class);
                });
    }

    @Override // GET /catalogs/:id
    public CompletionStage<GetCatalogResponse> getCatalog(String id) {
        return breaker.callWithCircuitBreakerCS(()->{
                    return service("/catalogs/"+urlEncode(id))
                            .get();
                })
                .handle((wsResponse, e) -> {
                    if(Objects.nonNull(e)) {
                        throw new RuntimeException(e);
                    }
                    return getObject(wsResponse.getBody(), GetCatalogResponse.class);
                });
    }

    @Override // POST /catalogs
    public CompletionStage<GetCatalogResponse> createCatalog(Http.Headers headers, CreateCatalogRequest request) {
        return breaker.callWithCircuitBreakerCS(()->{
                    return service("/catalogs")
                            .addHeader("Authorization", headers.get("Authorization").orElseGet(()->null))
                            .post(Json.toJson(request));
                })
                .handle((wsResponse, e) -> {
                    if(Objects.nonNull(e)) {
                        throw new RuntimeException(e);
                    }
                    return getObject(wsResponse.getBody(), GetCatalogResponse.class);
                });
    }

    @Override // PUT /catalogs
    public CompletionStage<GetCatalogResponse> updateCatalog(Http.Headers headers, UpdateCatalogRequest request) {
        return breaker.callWithCircuitBreakerCS(()->{
                    return service("/catalogs")
                            .addHeader("Authorization", headers.get("Authorization").orElseGet(()->null))
                            .put(Json.toJson(request));
                })
                .handle((wsResponse, e) -> {
                    if(Objects.nonNull(e)) {
                        throw new RuntimeException(e);
                    }
                    return getObject(wsResponse.getBody(), GetCatalogResponse.class);
                });
    }

    @Override // PATCH /catalogs/:id
    public CompletionStage<GetCatalogResponse> patchCatalog(Http.Headers headers, PatchCatalogRequest request, String id) {
        return breaker.callWithCircuitBreakerCS(()->{
                    return service("/catalogs/"+urlEncode(id))
                            .addHeader("Authorization", headers.get("Authorization").orElseGet(()->null))
                            .patch(Json.toJson(request));
                })
                .handle((wsResponse, e) -> {
                    if(Objects.nonNull(e)) {
                        throw new RuntimeException(e);
                    }
                    return getObject(wsResponse.getBody(), GetCatalogResponse.class);
                });
    }

    @Override // DELETE /catalogs/:id
    public CompletionStage<GetCatalogResponse> deleteCatalog(Http.Headers headers, String id) {
        return breaker.callWithCircuitBreakerCS(()->{
                    return service("/catalogs/"+urlEncode(id))
                            .addHeader("Authorization", headers.get("Authorization").orElseGet(()->null))
                            .delete();
                })
                .handle((wsResponse, e) -> {
                    if(Objects.nonNull(e)) {
                        throw new RuntimeException(e);
                    }
                    return getObject(wsResponse.getBody(), GetCatalogResponse.class);
                });
    }

    //DO NOT DELETE BELOW CODE SNIPPET
/*    public CompletionStage<Object> getInventoryUrlEncoded(MyRestClient.Feedback feedback){
        WSRequest request = ws
                .url("http://localhost/infoplus/api/v1/inventory")
                .setContentType("application/x-www-form-urlencoded");

        String body = "emailId="+urlEncode(feedback.emailId)+"&comment="+urlEncode(feedback.comment);

        return request.post(body)
                .thenApply(wsResponse -> {
                    return wsResponse.getBody();
                });
    }*/

}
