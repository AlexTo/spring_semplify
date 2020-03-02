package ai.semplify.entityhub.services;

import ai.semplify.entityhub.models.CrawlRequest;
import ai.semplify.entityhub.models.CrawlResponse;

public interface CrawlerService {

    CrawlResponse crawl(CrawlRequest request) throws Exception;

}
