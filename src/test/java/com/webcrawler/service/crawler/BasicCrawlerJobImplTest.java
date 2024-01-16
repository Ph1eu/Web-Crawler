package com.webcrawler.service.crawler;

import com.webcrawler.model.CrawlResult;
import com.webcrawler.service_impl.crawler.BasicCrawlerJobImpl;
import com.webcrawler.service_impl.crawler_scheduler.CrawlerSchedulerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class BasicCrawlerJobImplTest {

    @InjectMocks
    private BasicCrawlerJobImpl basicCrawlerJob;

    @Mock
    private CrawlerSchedulerImpl crawlerScheduler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Test StartCrawling Method")
    class StartCrawlingTest {

        @Test
        @DisplayName("When startCrawling is called, the URL should be added to the storage")
        void shouldExecuteTheMethod() {
            String url = "http://example.com";
            int depth = 2;
            CrawlResult initial = new CrawlResult(url, depth);

            basicCrawlerJob.setCrawlResult(initial);
            basicCrawlerJob.startCrawling();

            verify(crawlerScheduler).addUrlToStorage(initial);
        }
    }
}
