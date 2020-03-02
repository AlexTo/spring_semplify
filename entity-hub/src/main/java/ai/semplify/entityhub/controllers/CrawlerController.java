package ai.semplify.entityhub.controllers;

import ai.semplify.entityhub.models.CrawlRequest;
import ai.semplify.entityhub.models.CrawlResponse;
import ai.semplify.entityhub.services.CrawlerService;
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
