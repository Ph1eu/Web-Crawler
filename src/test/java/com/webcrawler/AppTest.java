package com.webcrawler;

import com.webcrawler.service.crawler.BasicCrawlerJobImplTest;
import com.webcrawler.service.crawler_scheduler.CrawlerSchedulerImplTest;
import com.webcrawler.service.url_storage.CrawledUrlStorageImplTest;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectClasses({
        BasicCrawlerJobImplTest.class,
        CrawlerSchedulerImplTest.class,
        CrawledUrlStorageImplTest.class
})
public class AppTest {

}
