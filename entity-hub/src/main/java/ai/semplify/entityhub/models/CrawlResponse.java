package ai.semplify.entityhub.models;

import lombok.Data;

import java.util.List;

@Data
public class CrawlResponse {

    private List<String> urls;
}
