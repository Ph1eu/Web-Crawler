package com.webcrawler.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.webcrawler.model.CrawlResult;
import com.webcrawler.service_impl.crawler_scheduler.CrawlerSchedulerImpl;
import com.webcrawler.utils.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class CrawlCommand {
    private final CrawlerSchedulerImpl crawlerScheduler;
    private static final Logger logger = LogManager.getLogger(CrawlCommand.class);
    @Autowired
    public CrawlCommand(CrawlerSchedulerImpl crawlerScheduler) {
        this.crawlerScheduler = crawlerScheduler;

    }

    @ShellMethod(key = "crawl", value = "Crawl a website")
    public void crawl(@ShellOption String url, @ShellOption(defaultValue = "2") int depth, @ShellOption(defaultValue = "1") int threads_count) {
        if (!UrlValidator.validateUrl(url)) {
            logger.error("Invalid URL: " + url);
            throw new IllegalArgumentException("Invalid URL: " + url);
        }
        if (depth <= 0) {
            logger.error("Depth must be greater than 0");
            throw new IllegalArgumentException("Depth must be greater than 0");
        }
        if (threads_count <= 0) {
            logger.error("Threads count must be greater than 0");
            throw new IllegalArgumentException("Threads count must be greater than 0");
        }
            CrawlResult initial = new CrawlResult(url, depth);
            crawlerScheduler.setThreadsCount(threads_count);
            crawlerScheduler.addInitialUrl(initial);
            crawlerScheduler.start();
    }
}
