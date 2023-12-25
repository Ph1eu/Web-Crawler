package org.example.scheduler;

import org.example.App;
import org.example.crawler.CrawlResult;
import org.example.crawler.CrawlerJob;
import org.example.storage.CrawledUrlStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

public class Scheduler {
    LinkedBlockingQueue<CrawlResult> queue;
    ExecutorService executorService;
    CrawledUrlStorage crawledUrlStorage;
    private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);

    public Scheduler(ExecutorService executorService, CrawledUrlStorage crawledUrlStorage) {
        this.queue = new LinkedBlockingQueue<CrawlResult>();
        this.executorService = executorService;
        this.crawledUrlStorage = crawledUrlStorage;
    }
    public void start(){

        while(true){
            CrawlResult crawlResult = getUrl();

            CrawlerJob crawlerJob = new CrawlerJob(crawlResult, this);
            executorService.submit(crawlerJob);
            //provide better termination logic here
            if(executorService.isTerminated()){
                break;
            }

        }
    }
    public void addInitialUrl(CrawlResult crawlResult){
        queue.add(crawlResult);
        logger.info("Added initial URL: " + crawlResult);
    }
    public boolean addUrlToStorage(CrawlResult url){
        if(crawledUrlStorage.isContained(url)){
            logger.info("URL already crawled: " + url);
            return false;
        }
        crawledUrlStorage.addResult(url);
        logger.info("Added URL to storage: " + url);
        return true;
    }
    public boolean AddUrlToQueue(CrawlResult crawlResult){
        if(crawlResult.getDepth() > 0){
            queue.add(crawlResult);
            logger.info("Added URL to queue: " + crawlResult);
            return true;
        }
        else{
            return false;
        }
    }

    public CrawlResult getUrl(){
        return queue.poll();
    }
}
