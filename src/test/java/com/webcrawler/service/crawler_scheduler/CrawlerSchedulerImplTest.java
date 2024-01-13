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

import static org.mockito.Mockito.verify;

public class CrawlerSchedulerImplTest {

    @InjectMocks
    private CrawlerSchedulerImpl crawlerScheduler;

    @Mock
    private CrawledUrlStorageImpl crawledUrlStorage;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


}
