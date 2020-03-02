package ai.semplify.entityhub.services.impl;

import ai.semplify.entityhub.config.CrawlerConfig;
import ai.semplify.entityhub.models.CrawlRequest;
import ai.semplify.entityhub.models.CrawlResponse;
import ai.semplify.entityhub.services.CrawlerService;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;
import lombok.var;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class CrawlerServiceImpl implements CrawlerService {

    private static final Pattern FILE_ENDING_EXCLUSION_PATTERN = Pattern.compile(".*(\\.(" +
            "css|js" +
            "|bmp|gif|jpe?g|JPE?G|png|tiff?|ico|nef|raw" +
            "|mid|mp2|mp3|mp4|wav|wma|flv|mpe?g" +
            "|avi|mov|mpeg|ram|m4v|wmv|rm|smil" +
            "|pdf|doc|docx|pub|xls|xlsx|vsd|ppt|pptx" +
            "|swf" +
            "|zip|rar|gz|bz2|7z|bin" +
            "|xml|txt|java|c|cpp|exe" +
            "))$");


    private CrawlerConfig crawlerConfig;

    public CrawlerServiceImpl(CrawlerConfig crawlerConfig) {
        this.crawlerConfig = crawlerConfig;
    }

    static class Crawler extends WebCrawler {

        private Function<Page, Boolean> pageVisitCallback;

        public Crawler(Function<Page, Boolean> pageVisitCallback) {
            this.pageVisitCallback = pageVisitCallback;
        }

        @Override
        public boolean shouldVisit(Page referringPage, WebURL url) {
            String href = url.getURL().toLowerCase();
            String referringDomain = referringPage.getWebURL().getDomain();
            String domain = url.getDomain();

            return !FILE_ENDING_EXCLUSION_PATTERN.matcher(href).matches() && domain.equalsIgnoreCase(referringDomain);
        }

        @Override
        public void visit(Page page) {
            super.visit(page);
            pageVisitCallback.apply(page);
        }
    }

    @Override
    public CrawlResponse crawl(CrawlRequest request) throws Exception {

        CrawlConfig config = new CrawlConfig();
        config.setMaxDepthOfCrawling(request.getDepth());
        config.setCrawlStorageFolder(crawlerConfig.getTmp());

        if (crawlerConfig.getProxyHost() != null) {
            config.setProxyHost(crawlerConfig.getProxyHost());
            if (crawlerConfig.getProxyPort() != null) {
                config.setProxyPort(crawlerConfig.getProxyPort());
            }
        }

        var pages = new ConcurrentLinkedQueue<Page>();

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        controller.addSeed(request.getUrl());

        CrawlController.WebCrawlerFactory<Crawler> factory = () -> new Crawler(pages::add);

        controller.start(factory, crawlerConfig.getNumberOfCrawlers());
        var response = new CrawlResponse();
        response.setUrls(pages.stream().map(p -> p.getWebURL().getURL()).collect(Collectors.toList()));
        return response;
    }
}
