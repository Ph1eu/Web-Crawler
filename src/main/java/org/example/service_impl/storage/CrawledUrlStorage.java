package org.example.service_impl.storage;

import org.example.model.CrawlResult;
import org.example.service.url_storage.IUrlStorage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
@Component
public class CrawledUrlStorage implements IUrlStorage {
    private ConcurrentSkipListSet<CrawlResult> crawledUrls;

    public CrawledUrlStorage() {
        this.crawledUrls = new ConcurrentSkipListSet<CrawlResult>();
    }
    @Override
    public boolean isContained(CrawlResult url){
        return crawledUrls.contains(url);
    }

    @Override
    public void addUrl(CrawlResult url) {
        crawledUrls.add(url);
    }
    @Override
    public boolean isEmpty() {
        return this.crawledUrls.isEmpty();
    }
    @Override
    public int getSize(){
        return crawledUrls.size();
    }
    @Override
    public List<String> getUrls(){
        List<String> urls = new java.util.ArrayList<String>();
        for(CrawlResult crawlResult : crawledUrls){
            urls.add(crawlResult.getUrl());
        }
        return urls;
    }
}
