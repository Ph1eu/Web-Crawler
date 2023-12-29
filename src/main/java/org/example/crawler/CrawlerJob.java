package org.example.crawler;

import org.example.scheduler.Scheduler;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CrawlerJob implements Runnable {
    private CrawlResult crawlResult;
    private Scheduler scheduler;
    private static final Logger logger = LoggerFactory.getLogger(CrawlerJob.class);

    public CrawlerJob(CrawlResult crawlResult, Scheduler scheduler) {
        this.crawlResult = crawlResult;
        this.scheduler = scheduler;
    }
    public  void crawl(String url,Integer depth) throws IOException {
        if (depth == 1) {
            scheduler.decrementDepthOneTasks();
        }
        Document document = request(url);
        Elements links = document.select("a[href]");
        System.out.println("Received web page at " + url);
        System.out.println("Title: " + document.title());
        CrawlResult currentCrawlResult = new CrawlResult(url,depth);
        if(scheduler.isContainedInStorage(currentCrawlResult)){
            logger.info("URL already crawled: " + url);
            return;
        }
        else{
            scheduler.addUrlToStorage(currentCrawlResult);
        }
        for (Element link : links) {
            String nextUrl = link.attr("abs:href");
            if(nextUrl == null || nextUrl.isEmpty()){
                continue;
            }
            CrawlResult childCrawlResult = new CrawlResult(nextUrl,depth-1);
            if(!scheduler.isUrlMaxDepth(childCrawlResult)){
                scheduler.AddUrlToQueue(childCrawlResult);
            }
        }
    }
     private  Document request(String url) throws IOException {
        Connection connection = Jsoup.connect(url);
        Connection.Response response = connection.execute();
        Document document = connection.get();
        if(response.statusCode() == 200){
            System.out.println("Received web page at " + url);
            System.out.println("Title: " + document.title());
            int size = response.bodyAsBytes().length;
            scheduler.incrementTotalDownloadedBytes(size);
            System.out.println("Size: " + size + " bytes");
            return document;
        } else {
            throw new IOException("HTTP status code: " + connection.response().statusCode());
        }
    }

    @Override
    public void run() {
        try {
            crawl(crawlResult.getUrl(), crawlResult.getDepth());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
