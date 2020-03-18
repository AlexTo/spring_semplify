package ai.semplify.commons.models.fileserver;

import lombok.Data;

import java.util.List;

@Data
public class FileAnnotationPage {
    private List<FileAnnotation> fileAnnotations;
    private Long totalElements;
    private Integer totalPages;
    private Boolean hasNext;
    private Boolean hasPrevious;
    private Boolean isEmpty;
}
