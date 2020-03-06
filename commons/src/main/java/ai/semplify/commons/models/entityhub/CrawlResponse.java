package ai.semplify.commons.models.entityhub;

import lombok.Data;

import java.util.List;

@Data
public class CrawlResponse {

    private List<String> urls;
}
