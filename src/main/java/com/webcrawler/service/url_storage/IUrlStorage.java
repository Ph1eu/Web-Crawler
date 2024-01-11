package com.webcrawler.service.url_storage;

import com.webcrawler.model.CrawlResult;


public interface IUrlStorage {
    void addUrl(CrawlResult url);
    int getSize();
    boolean isContained(CrawlResult url);

}
