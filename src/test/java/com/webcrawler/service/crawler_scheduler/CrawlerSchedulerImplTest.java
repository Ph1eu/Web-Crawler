package com.webcrawler.service.crawler_scheduler;

import com.webcrawler.model.CrawlResult;
import com.webcrawler.service_impl.crawler_scheduler.CrawlerSchedulerImpl;
import com.webcrawler.service_impl.url_storage.CrawledUrlStorageImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CrawlerSchedulerImplTest {

    @InjectMocks
    private CrawlerSchedulerImpl crawlerScheduler;

    @Mock
    private CrawledUrlStorageImpl crawledUrlStorage;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Test addInitialUrl method")
    class AddInitialUrlTest {
        @Test
        @DisplayName("When adding an initial URL, it should be in the queue")
        void shouldAddInitialUrlToQueue() {
            CrawlResult crawlResult = new CrawlResult("http://example.com", 1);
            crawlerScheduler.addInitialUrl(crawlResult);
            assertSame(crawlerScheduler.getUrl(), crawlResult);
        }
    }
}
