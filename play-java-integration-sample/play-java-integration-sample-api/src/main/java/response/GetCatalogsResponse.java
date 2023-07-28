package response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class GetCatalogsResponse {

    private Pagination pagination;
    private List<GetCatalogResponse> catalogs;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Pagination {
        private Integer totalCount;
        private Integer limit;
        private Integer offset;
    }
}
