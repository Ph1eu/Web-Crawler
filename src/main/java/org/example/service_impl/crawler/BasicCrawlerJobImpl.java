package org.example.service_impl.crawler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.model.CrawlResult;
import org.example.service.crawler.AbstractCrawlerJob;
import org.example.service_impl.crawler_scheduler.CrawlerSchedulerImpl;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
public class BasicCrawlerJobImpl extends AbstractCrawlerJob  {
    private static final Logger logger = LogManager.getLogger(BasicCrawlerJobImpl.class);
    @Autowired
    public BasicCrawlerJobImpl(CrawlerSchedulerImpl crawlerScheduler) {
        super(crawlerScheduler);
    }

    public  void BasicCrawl(String url,Integer depth) throws IOException {
        if (depth == 1) {
            this.crawlerScheduler.decrementDepthOneTasks();
        }
        Document document = fetchDocument(url);
        Elements links = linkExtractor(document);
        CrawlResult currentCrawlResult = new CrawlResult(url,depth);
        if(crawlerScheduler.isContainedInStorage(currentCrawlResult)){
            logger.info("URL already crawled: " + url);
            return;
        }
        else{
            crawlerScheduler.addUrlToStorage(currentCrawlResult);
        }
        for (Element link : links) {
            String nextUrl = link.attr("abs:href");
            if(nextUrl.isEmpty()){
                continue;
            }
            CrawlResult childCrawlResult = new CrawlResult(nextUrl,depth-1);
            if(!crawlerScheduler.isUrlMaxDepth(childCrawlResult)){
                crawlerScheduler.AddUrlToQueue(childCrawlResult);
            }
        }
    }
    @Override
    public void run() {
        this.startCrawling();
    }

    @Override
    public void startCrawling() {
        try {
            BasicCrawl(crawlResult.getUrl(), crawlResult.getDepth());
        } catch (IOException e) {
            logger.error("Error while crawling " + crawlResult.getUrl(), e);
        }
    }



    @Override
    public Elements linkExtractor(Document document) {
        return document.select("a[href]");
    }
    @Override
    public Document fetchDocument(String url) throws IOException {
        Connection connection = Jsoup.connect(url);
        Connection.Response response = connection.execute();
        Document document = connection.get();
        if(response.statusCode() == 200){
            System.out.println("Received web page at " + url);
            System.out.println("Title: " + document.title());
            int size = response.bodyAsBytes().length;
            crawlerScheduler.incrementTotalDownloadedBytes(size);
            System.out.println("Size: " + size + " bytes");
            return document;
        } else {
            throw new IOException("HTTP status code: " + connection.response().statusCode());
        }    }
}
