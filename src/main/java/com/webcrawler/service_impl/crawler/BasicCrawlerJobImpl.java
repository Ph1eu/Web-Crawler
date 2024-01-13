package com.webcrawler.service_impl.crawler;

import com.webcrawler.model.CrawlResult;
import com.webcrawler.service.crawler.AbstractCrawlerJob;
import com.webcrawler.service_impl.crawler_scheduler.CrawlerSchedulerImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
/**
 * This class extends AbstractCrawlerJob and provides the implementation for a basic web crawler job.
 * It uses the Jsoup library to fetch and parse HTML documents.
 * It also uses the CrawlerScheduler to manage the crawling tasks.
 */
@Service
public class BasicCrawlerJobImpl extends AbstractCrawlerJob {
    private static final Logger logger = LogManager.getLogger(BasicCrawlerJobImpl.class);
    /**
     * Constructor for BasicCrawlerJobImpl.
     * @param crawlerScheduler the scheduler used to manage the crawling tasks.
     */
    @Autowired
    public BasicCrawlerJobImpl(CrawlerSchedulerImpl crawlerScheduler) {
        super(crawlerScheduler);
    }
    /**
     * This method performs the basic crawl operation.
     * It fetches the document at the given URL, extracts the links, and adds them to the crawl queue.
     * @param url the URL to crawl.
     * @param depth the depth of the crawl.
     * @throws IOException if an error occurs while fetching the document.
     */
    public  void basicCrawl(String url,Integer depth) throws IOException {
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
            basicCrawl(crawlResult.getUrl(), crawlResult.getDepth());
        } catch (IOException e) {
            logger.error("Error while crawling " + crawlResult.getUrl(), e);
        }
    }
    /**
     * This method extracts the links from the given document.
     * @param document the document to extract links from.
     * @return a list of Elements representing the links.
     */
    @Override
    public Elements linkExtractor(Document document) {
        return document.select("a[href]");
    }
    /**
     * This method fetches the document at the given URL.
     * It also logs the size of the downloaded document.
     * @param url the URL to fetch the document from.
     * @return the fetched document.
     * @throws IOException if an error occurs while fetching the document.
     */
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

    @Override
    public String toString() {
        return "BasicCrawlerJobImpl{" +
                "crawlResult=" + crawlResult +
                '}';
    }
}
