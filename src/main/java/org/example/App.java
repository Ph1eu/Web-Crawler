package org.example;

import org.example.model.CrawlResult;
import org.example.service_impl.crawler_scheduler.CrawlerSchedulerImpl;
import org.example.service_impl.storage.CrawledUrlStorage;
import org.example.utils.Validator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Hello world!
 */
@SpringBootApplication
public class App {
 //   private static final Integer MAX_LEVEL = 5;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter URL: ");
//        String url = scanner.nextLine();
//        System.out.println("Enter depth: ");
//        Integer depth = scanner.nextInt();
//        System.out.println("Enter number of threads: ");
//        int thread_count = scanner.nextInt();
//        if (!Validator.validateUrl(url)) {
//            System.out.println("Invalid URL");
//            return;
//        }
//        if (depth > MAX_LEVEL) {
//            System.out.println("Depth exceeds 5");
//            return;
//        }
//        CrawlResult initial = new CrawlResult(url, depth);
//        ExecutorService executorService = Executors.newFixedThreadPool(thread_count);
//        CrawledUrlStorage crawledUrlStorage = new CrawledUrlStorage();
//        CrawlerSchedulerImpl crawlerSchedulerImpl = new CrawlerSchedulerImpl(executorService, crawledUrlStorage);
//        crawlerSchedulerImpl.addInitialUrl(initial);
//        crawlerSchedulerImpl.start();
    }
}
