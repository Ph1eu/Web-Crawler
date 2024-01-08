package com.webcrawler.service.crawler;

import com.webcrawler.model.CrawlResult;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public interface ICrawlerJob extends Runnable{
    void setCrawlResult(CrawlResult url);
    void startCrawling();
    Elements linkExtractor (Document document);
    Document fetchDocument (String url) throws IOException;

}
