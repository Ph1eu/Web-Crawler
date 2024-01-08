package com.webcrawler.service.crawler;

import com.webcrawler.model.CrawlResult;
import com.webcrawler.service_impl.crawler_scheduler.CrawlerSchedulerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public abstract class AbstractCrawlerJob implements ICrawlerJob {
    protected CrawlerSchedulerImpl crawlerScheduler;
    protected CrawlResult crawlResult;
    @Autowired
    public AbstractCrawlerJob(CrawlerSchedulerImpl crawlerScheduler ) {
        this.crawlerScheduler = crawlerScheduler;
    }
    @Override
    public void setCrawlResult(CrawlResult crawlResult) {
        this.crawlResult = crawlResult;
    }
}
