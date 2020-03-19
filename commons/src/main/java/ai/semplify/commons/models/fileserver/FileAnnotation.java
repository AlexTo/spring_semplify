package ai.semplify.commons.models.fileserver;

import ai.semplify.commons.models.entityhub.AnnotationResource;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class FileAnnotation {
    private Long id;

    @NotNull
    private File file;

    private String status;

    private List<AnnotationResource> annotationResources;

}
