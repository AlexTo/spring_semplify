package ai.semplify.entityhub.services;

import ai.semplify.commons.models.entityhub.CrawlRequest;
import ai.semplify.commons.models.entityhub.CrawlResponse;

public interface CrawlerService {

    CrawlResponse crawl(CrawlRequest request) throws Exception;

}
