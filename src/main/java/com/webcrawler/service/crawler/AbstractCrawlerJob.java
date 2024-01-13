package com.webcrawler.service.crawler;

import com.webcrawler.model.CrawlResult;
import com.webcrawler.service_impl.crawler_scheduler.CrawlerSchedulerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This abstract class implements the ICrawlerJob interface and provides a base for all crawler jobs.
 * It contains a reference to the CrawlerScheduler and the current CrawlResult.
 * The CrawlerScheduler is used to manage the crawling tasks.
 * The CrawlResult contains the URL and depth of the current crawl task.
 */
@Component
public abstract class AbstractCrawlerJob implements ICrawlerJob {
    protected CrawlerSchedulerImpl crawlerScheduler;
    protected CrawlResult crawlResult;
    /**
     * Constructor for AbstractCrawlerJob.
     * @param crawlerScheduler the scheduler used to manage the crawling tasks.
     */
    @Autowired
    public AbstractCrawlerJob(CrawlerSchedulerImpl crawlerScheduler ) {
        this.crawlerScheduler = crawlerScheduler;
    }
    @Override
    public void setCrawlResult(CrawlResult crawlResult) {
        this.crawlResult = crawlResult;
    }
}
