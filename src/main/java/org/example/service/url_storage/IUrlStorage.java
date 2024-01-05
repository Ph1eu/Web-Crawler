package org.example.service.url_storage;

import org.example.model.CrawlResult;

import java.util.List;

public interface IUrlStorage {
    void addUrl(CrawlResult url);
    List<String> getUrls();
    boolean isEmpty();
    int getSize();
    boolean isContained(CrawlResult url);

}
