package org.example.commands;

import org.example.model.CrawlResult;
import org.example.service_impl.crawler_scheduler.CrawlerSchedulerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class CrawCommand {
    private final CrawlerSchedulerImpl crawlerScheduler;
    @Autowired
    public CrawCommand(CrawlerSchedulerImpl crawlerScheduler) {
        this.crawlerScheduler = crawlerScheduler;
    }

    @ShellMethod(key = "crawl", value = "Crawl a website")
    public void crawl(@ShellOption String url, @ShellOption(defaultValue = "1") int depth, @ShellOption(defaultValue = "1") int threads_count) {
        CrawlResult initial = new CrawlResult(url, depth);
        // set thread count app config executor service
        crawlerScheduler.setThreadsCount(threads_count);
        crawlerScheduler.addInitialUrl(initial);
        crawlerScheduler.start();
    }
}
