package org.example.storage;

import org.example.crawler.CrawlResult;

import java.util.concurrent.ConcurrentSkipListSet;

public class CrawledUrlStorage {
    ConcurrentSkipListSet<CrawlResult> crawledUrls;

    public CrawledUrlStorage() {
        this.crawledUrls = new ConcurrentSkipListSet<CrawlResult>();
    }
    public void addResult(CrawlResult url){
        crawledUrls.add(url);
    }
    public boolean isContained(CrawlResult url){
        return crawledUrls.contains(url);
    }
}
