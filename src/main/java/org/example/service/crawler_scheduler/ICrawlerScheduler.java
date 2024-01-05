package org.example.service.crawler_scheduler;

import org.springframework.stereotype.Component;

@Component
public interface ICrawlerScheduler {
    void start();
    boolean stop();
}
