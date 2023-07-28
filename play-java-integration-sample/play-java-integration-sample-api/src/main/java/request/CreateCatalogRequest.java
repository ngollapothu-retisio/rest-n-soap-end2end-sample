package request;

import lombok.Value;

@Value
public class CreateCatalogRequest {
    private String catalogId;
    private String catalogName;
    private Boolean active;
}
