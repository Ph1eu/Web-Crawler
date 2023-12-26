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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static java.lang.Thread.sleep;

public class Scheduler {
    private LinkedBlockingQueue<CrawlResult> queue;
    private ExecutorService executorService;
    private CrawledUrlStorage crawledUrlStorage;
    private AtomicInteger depthOneTasks;

    private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);

    public Scheduler(ExecutorService executorService, CrawledUrlStorage crawledUrlStorage) {
        this.queue = new LinkedBlockingQueue<CrawlResult>();
        this.executorService = executorService;
        this.crawledUrlStorage = crawledUrlStorage;
        this.depthOneTasks = new AtomicInteger(0);

    }
    public void start(){
        while(true){
            CrawlResult crawlResult = getUrl();
            if (crawlResult.getDepth() == 1) {
                depthOneTasks.incrementAndGet();
                logger.info("Depth one tasks: " + depthOneTasks.get());
            }
            CrawlerJob crawlerJob = new CrawlerJob(crawlResult, this);
            executorService.submit(crawlerJob);
            logger.info("Current queue size: " + queue.size());
            //provide termination condition when all crawl results depth are 0
            if (crawlResult.getDepth() == 1) {
                depthOneTasks.decrementAndGet();
                logger.info("Depth one tasks: " + depthOneTasks.get());
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (depthOneTasks.get() == 0 && queue.isEmpty() && !executorService.isShutdown() ) {
                executorService.shutdown();
                logger.info("Shutting down executor service");
                break;
            }

        }
    }
    public void addInitialUrl(CrawlResult crawlResult){
        try{
        queue.put(crawlResult);
        logger.info("Added initial URL: " + crawlResult);}
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
