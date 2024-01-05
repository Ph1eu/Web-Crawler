package org.example.service.crawler;

import org.example.model.CrawlResult;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public interface ICrawlerJob extends Runnable{
    void setCrawlResult(CrawlResult url);
    void startCrawling();
    void stopCrawling();
    Elements linkExtractor (Document document);
    Document fetchDocument (String url) throws IOException;
    void parse (Document document);

}
