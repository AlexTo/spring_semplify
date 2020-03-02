package ai.semplify.indexer.models;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SuggestionRequest {

    @NotEmpty
    private String text;

    @NotEmpty
    private String type;

}
