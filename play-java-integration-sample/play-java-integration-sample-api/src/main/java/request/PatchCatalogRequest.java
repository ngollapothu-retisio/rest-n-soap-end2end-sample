package request;

import lombok.Value;

@Value
public class PatchCatalogRequest {
    private String catalogName;
    private Boolean active;
}
