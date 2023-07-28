package service;

import play.mvc.Http;
import request.CreateCatalogRequest;
import request.PatchCatalogRequest;
import request.UpdateCatalogRequest;
import response.GetCatalogResponse;
import response.GetCatalogsResponse;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface CatalogService {
    public CompletionStage<GetCatalogsResponse> getCatalogs(Optional<String> filter, Optional<String> limit, Optional<String> offset);
    public CompletionStage<GetCatalogResponse> getCatalog(String id);
    public CompletionStage<GetCatalogResponse> createCatalog(Http.Headers headers, CreateCatalogRequest request);
    public CompletionStage<GetCatalogResponse> updateCatalog(Http.Headers headers, UpdateCatalogRequest request);
    public CompletionStage<GetCatalogResponse> patchCatalog(Http.Headers headers, PatchCatalogRequest request, String id);
    public CompletionStage<GetCatalogResponse> deleteCatalog(Http.Headers headers, String id);
}
