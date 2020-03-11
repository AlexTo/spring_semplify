package ai.semplify.indexer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.crawler")
public class CrawlerConfig {
    private Integer numberOfCrawlers;
    private String proxyHost;
    private Integer proxyPort;
    private String tmp;
}
