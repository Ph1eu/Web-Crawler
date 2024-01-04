package org.example.service_impl.storage;

import org.example.model.CrawlResult;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

public class CrawledUrlStorage {
    private ConcurrentSkipListSet<CrawlResult> crawledUrls;

    public CrawledUrlStorage() {
        this.crawledUrls = new ConcurrentSkipListSet<CrawlResult>();
    }
    public void addResult(CrawlResult url){
        crawledUrls.add(url);
    }
    public boolean isContained(CrawlResult url){
        return crawledUrls.contains(url);
    }
    public Integer getSize(){
        return crawledUrls.size();
    }
    public List<String> getUrls(){
        List<String> urls = new java.util.ArrayList<String>();
        for(CrawlResult crawlResult : crawledUrls){
            urls.add(crawlResult.getUrl());
        }
        return urls;
    }
}
