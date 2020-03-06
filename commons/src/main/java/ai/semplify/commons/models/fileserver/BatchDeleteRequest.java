package ai.semplify.commons.models.fileserver;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BatchDeleteRequest {
    @NotNull
    private List<Long> fileIds;
}
