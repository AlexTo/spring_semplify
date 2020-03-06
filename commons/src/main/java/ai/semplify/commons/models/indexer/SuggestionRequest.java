package ai.semplify.commons.models.indexer;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SuggestionRequest {

    @NotEmpty
    private String text;

    @NotEmpty
    private String type;

}
