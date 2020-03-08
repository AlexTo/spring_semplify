package ai.semplify.feignclients.clients.fileserver.models;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class BatchDeleteRequest {
    @NotNull
    private List<Long> fileIds;
}
