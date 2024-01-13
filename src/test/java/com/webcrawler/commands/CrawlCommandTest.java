package com.webcrawler.commands;

import com.webcrawler.model.CrawlResult;
import com.webcrawler.service_impl.crawler_scheduler.CrawlerSchedulerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class CrawlCommandTest {

    @InjectMocks
    private CrawlCommand crawlCommand;

    @Mock
    private CrawlerSchedulerImpl crawlerScheduler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrawl() {
        String url = "http://example.com";
        int depth = 2;
        int threads_count = 1;
        CrawlResult initial = new CrawlResult(url, depth);

        crawlCommand.crawl(url, depth, threads_count);

        verify(crawlerScheduler).setThreadsCount(threads_count);
        verify(crawlerScheduler).addInitialUrl(initial);
        verify(crawlerScheduler).start();
    }
}