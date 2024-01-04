package org.example.service_impl.scheduler;


import org.example.model.CrawlResult;
import org.example.service_impl.crawler.CrawlerJob;
import org.example.service_impl.storage.CrawledUrlStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class Scheduler {
    private LinkedBlockingQueue<CrawlResult> queue;
    private ExecutorService executorService;
    private CrawledUrlStorage crawledUrlStorage;
    private AtomicInteger depthOneTasks;
    private AtomicInteger totalDownloadedBytes;
    private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);

    public Scheduler(ExecutorService executorService, CrawledUrlStorage crawledUrlStorage) {
        this.queue = new LinkedBlockingQueue<CrawlResult>();
        this.executorService = executorService;
        this.crawledUrlStorage = crawledUrlStorage;
        this.depthOneTasks = new AtomicInteger(0);
        this.totalDownloadedBytes = new AtomicInteger(0);

    }
    public void start(){
        while(true){
            CrawlResult crawlResult = getUrl();
            if (crawlResult.getDepth() == 1) {
                incrementDepthOneTasks();
            }
            CrawlerJob crawlerJob = new CrawlerJob(crawlResult, this);
            executorService.submit(crawlerJob);
            logger.info("Current queue size: " + queue.size());
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
        logger.info("Total downloaded bytes: " + totalDownloadedBytes.get());
        logger.info("Total number of URLs: " + crawledUrlStorage.getSize());
    }
    public void addInitialUrl(CrawlResult crawlResult){
        try{
        queue.put(crawlResult);
        logger.info("Added initial URL: " + crawlResult);}
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void incrementTotalDownloadedBytes(Integer bytes){
        totalDownloadedBytes.addAndGet(bytes);
        logger.info("Incremented total downloaded bytes with current value is "+totalDownloadedBytes.get());
    }

    public void incrementDepthOneTasks(){
        depthOneTasks.incrementAndGet();
        logger.info("Incremented depth one tasks with current value is "+depthOneTasks.get());
    }
    public void decrementDepthOneTasks(){
        depthOneTasks.decrementAndGet();
        logger.info("Decremented depth one tasks with current value is "+depthOneTasks.get());
    }
    public boolean isContainedInStorage(CrawlResult crawlResult){
        if (crawledUrlStorage.isContained(crawlResult)){
            return true;
        }
        else {
            return false;
        }
    }
    public void  addUrlToStorage(CrawlResult url){
        crawledUrlStorage.addResult(url);
        logger.info("Added URL to storage: " + url);
    }
    public boolean isUrlMaxDepth(CrawlResult crawlResult){
        if(crawlResult.getDepth() == 0){
            return true;
        }
        else{
            return false;
        }
    }
    public void AddUrlToQueue(CrawlResult crawlResult){
            queue.add(crawlResult);
            logger.info("Added URL to queue: " + crawlResult);
    }

    public CrawlResult getUrl(){
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
