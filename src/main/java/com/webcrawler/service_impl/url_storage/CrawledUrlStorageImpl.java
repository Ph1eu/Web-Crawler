package com.webcrawler.service_impl.url_storage;

import com.webcrawler.model.CrawlResult;
import com.webcrawler.service.url_storage.IUrlStorage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
@Service
public class CrawledUrlStorageImpl implements IUrlStorage {
    private final ConcurrentSkipListSet<CrawlResult> crawledUrls;

    public CrawledUrlStorageImpl() {
        this.crawledUrls = new ConcurrentSkipListSet<>();
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
        List<String> urls = new java.util.ArrayList<>();
        for(CrawlResult crawlResult : crawledUrls){
            urls.add(crawlResult.getUrl());
        }
        return urls;
    }
}
