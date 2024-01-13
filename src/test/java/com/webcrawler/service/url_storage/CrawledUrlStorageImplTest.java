package com.webcrawler.service.url_storage;

import com.webcrawler.model.CrawlResult;
import com.webcrawler.service_impl.url_storage.CrawledUrlStorageImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CrawledUrlStorageImplTest {

    private CrawledUrlStorageImpl crawledUrlStorage;

    @BeforeEach
    public void setup() {
        crawledUrlStorage = new CrawledUrlStorageImpl();
    }

    @Nested
    @DisplayName("Test AddUrl Method")
    class AddUrlTest {

        @Test
        @DisplayName("When a URL is added, it should be contained in the storage")
        public void testAddUrl() {
            String url = "http://example.com";
            int depth = 2;
            CrawlResult crawlResult = new CrawlResult(url, depth);

            assertFalse(crawledUrlStorage.isContained(crawlResult));
            crawledUrlStorage.addUrl(crawlResult);
            assertTrue(crawledUrlStorage.isContained(crawlResult));
        }
    }
    @Nested
    @DisplayName("Test GetSize Method")
    class GetSizeTest {

        @Test
        @DisplayName("When a URL is added, the size of the storage should increase")
        public void testGetSize() {
            assertEquals(0, crawledUrlStorage.getSize());

            String url = "http://example.com";
            int depth = 2;
            CrawlResult crawlResult = new CrawlResult(url, depth);

            crawledUrlStorage.addUrl(crawlResult);
            assertEquals(1, crawledUrlStorage.getSize());
        }
    }
}