package com.webcrawler.service_impl.crawler_scheduler;


import com.webcrawler.model.CrawlResult;
import com.webcrawler.service.crawler_scheduler.ICrawlerScheduler;
import com.webcrawler.service_impl.url_storage.CrawledUrlStorageImpl;
import com.webcrawler.service_impl.crawler.BasicCrawlerJobImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The CrawlerSchedulerImpl class oversees and executes web crawl tasks, implementing the ICrawlerScheduler interface.
 * It uses a thread-safe LinkedBlockingQueue for task management and CrawledUrlStorageImpl to prevent duplicate crawling.
 * The class includes crucial fields like thread count and flags, offering methods for starting, stopping crawling,
 * adding URLs, managing tasks, and checking storage.
 */
@Service
public class CrawlerSchedulerImpl implements ICrawlerScheduler {
    private int Threads_count = 1;
    private final LinkedBlockingQueue<CrawlResult> queue = new LinkedBlockingQueue<>();
    private ExecutorService executorService ;
    private final CrawledUrlStorageImpl crawledUrlStorageImpl;
    private final AtomicInteger depthOneTasks;
    private final AtomicInteger totalDownloadedBytes ;
    private boolean stop = false;
    private static final Logger logger = LogManager.getLogger(CrawlerSchedulerImpl.class);

    @Autowired
    public CrawlerSchedulerImpl( CrawledUrlStorageImpl crawledUrlStorageImpl) {
        this.crawledUrlStorageImpl = crawledUrlStorageImpl;
        this.depthOneTasks = new AtomicInteger(0);
        this.totalDownloadedBytes = new AtomicInteger(0);
    }
    public void setThreadsCount(int threadsCount){
        this.Threads_count = threadsCount;
    }
    /**
     * Starts the crawling process by scheduling two tasks.
     * The first task retrieves a CrawlResult and submits a new BasicCrawlerJobImpl for execution.
     * The second task stops the process if there are no more depth one tasks and the queue is empty, then logs the results.
     */
    @Override
    public void start() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        this.executorService = Executors.newFixedThreadPool(Threads_count);
        scheduler.scheduleAtFixedRate(() -> {
            if (!stop) {
                CrawlResult crawlResult = getUrl();
                if (crawlResult.getDepth() == 1) {
                    incrementDepthOneTasks();
                }
                BasicCrawlerJobImpl crawlerJob = new BasicCrawlerJobImpl(this);
                crawlerJob.setCrawlResult(crawlResult);
                logger.info("Current queue size: " + queue.size());
                executorService.submit(()->{
                    try {
                        crawlerJob.run();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                }
                });
            }}, 0, 1, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(() -> {
                stop();
                if(stop && executorService.isTerminated()){
                    logger.info("Total downloaded bytes: " + totalDownloadedBytes.get());
                    logger.info("Total number of URLs: " + crawledUrlStorageImpl.getSize());
                    scheduler.shutdown();
                }
            }
            , 2, 3, TimeUnit.SECONDS);
    }
    @Override
    public void stop() {
        if (depthOneTasks.get() == 0 && queue.isEmpty() && !executorService.isShutdown()) {
            executorService.shutdown();
            logger.info("Shutting down executor service");
            stop = true;
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
