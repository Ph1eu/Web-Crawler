package com.webcrawler.service_impl.url_storage;

import com.webcrawler.model.CrawlResult;
import com.webcrawler.service.url_storage.IUrlStorage;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentSkipListSet;

/**
 * This class implements the IUrlStorage interface and provides a thread-safe storage for crawled URLs.
 * It uses a ConcurrentSkipListSet to store the URLs, ensuring that the URLs are sorted and duplicate URLs are not stored.
 */
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
    public int getSize(){
        return crawledUrls.size();
    }

}
