package com.webcrawler.service_impl.crawler_scheduler;


import com.webcrawler.model.CrawlResult;
import com.webcrawler.service.crawler.AbstractCrawlerJob;
import com.webcrawler.service.crawler_scheduler.ICrawlerScheduler;
import com.webcrawler.service_impl.url_storage.CrawledUrlStorageImpl;
import com.webcrawler.service_impl.crawler.BasicCrawlerJobImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CrawlerSchedulerImpl implements ICrawlerScheduler {
    private final LinkedBlockingQueue<CrawlResult> queue = new LinkedBlockingQueue<>();
    private ExecutorService executorService ;
    private final CrawledUrlStorageImpl crawledUrlStorageImpl;
    private final AtomicInteger depthOneTasks;
    private final AtomicInteger totalDownloadedBytes ;
    private CountDownLatch latch ;
    private static final Logger logger = LogManager.getLogger(CrawlerSchedulerImpl.class);

    @Autowired
    public CrawlerSchedulerImpl( CrawledUrlStorageImpl crawledUrlStorageImpl) {
        this.executorService = null;
        this.crawledUrlStorageImpl = crawledUrlStorageImpl;
        this.depthOneTasks = new AtomicInteger(0);
        this.totalDownloadedBytes = new AtomicInteger(0);
        this.latch = new CountDownLatch(1);
    }
    public void setThreadsCount(int threadsCount){
        this.executorService = Executors.newFixedThreadPool(threadsCount);
    }
    @Override
    public void start() {
        while (!stop()) {
            CrawlResult crawlResult = getUrl();
            if (crawlResult.getDepth() == 1) {
                incrementDepthOneTasks();
            }
            AbstractCrawlerJob crawlerJob = new BasicCrawlerJobImpl( this);
            crawlerJob.setCrawlResult(crawlResult);
            executorService.submit(() -> {
                try {
                    crawlerJob.run();
                } finally {
                    latch.countDown();
                }
            });
            latch = new CountDownLatch((int) latch.getCount() + 1);
            logger.info("Current queue size: " + queue.size());
        }
        logger.info("Total downloaded bytes: " + totalDownloadedBytes.get());
        logger.info("Total number of URLs: " + crawledUrlStorageImpl.getSize());
    }

    @Override
    public boolean stop() {
        if (depthOneTasks.get() == 0 && queue.isEmpty() && !executorService.isShutdown()) {
            try {
                latch.await(); // wait for all tasks to finish
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            executorService.shutdown();
            logger.info("Shutting down executor service");
        }
        return executorService.isShutdown();
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
        return crawledUrlStorageImpl.isContained(crawlResult);
    }
    public void  addUrlToStorage(CrawlResult url){
        crawledUrlStorageImpl.addUrl(url);
        logger.info("Added URL to storage: " + url);
    }
    public boolean isUrlMaxDepth(CrawlResult crawlResult){
        return crawlResult.getDepth() == 0;
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
