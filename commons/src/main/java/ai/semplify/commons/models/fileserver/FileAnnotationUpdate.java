package ai.semplify.commons.models.fileserver;

import ai.semplify.commons.models.entityhub.AnnotationResource;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class FileAnnotationUpdate {
    @NotNull
    private Long id;

    @NotNull
    private String status;

    @NotNull
    private List<AnnotationResource> annotationResources;
}
