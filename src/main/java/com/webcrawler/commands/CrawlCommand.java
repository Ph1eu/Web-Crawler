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
    public void crawl(@ShellOption String url, @ShellOption(defaultValue = "2",help = "depth level for crawling") int depth, @ShellOption(defaultValue = "1",help = "Number of threads for crawling") int threads_count) {
            validateInputs(url, depth, threads_count);
            CrawlResult initial = new CrawlResult(url, depth);
            crawlerScheduler.setThreadsCount(threads_count);
            crawlerScheduler.addInitialUrl(initial);
            crawlerScheduler.start();
    }
    private void validateInputs(String url, int depth, int threads_count) {
        if (!UrlValidator.validateUrl(url)) {
            String errorMessage = String.format("Invalid URL: %s", url);
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        if (depth <= 0) {
            String errorMessage = "Depth must be greater than 0";
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        if (threads_count <= 0) {
            String errorMessage = "Threads count must be greater than 0";
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
