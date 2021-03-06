package ai.semplify.indexer.controllers;

import ai.semplify.commons.models.entityhub.CrawlRequest;
import ai.semplify.commons.models.entityhub.CrawlResponse;
import ai.semplify.indexer.services.CrawlerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/crawler")
public class CrawlerController {

    private CrawlerService crawlerService;

    public CrawlerController(CrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    @PostMapping("")
    public ResponseEntity<CrawlResponse> crawl(@Valid @RequestBody CrawlRequest request) throws Exception {
        return ResponseEntity.ok(crawlerService.crawl(request));
    }

}
