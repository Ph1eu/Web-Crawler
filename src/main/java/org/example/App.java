package org.example;

import org.example.model.CrawlResult;
import org.example.service_impl.scheduler.Scheduler;
import org.example.service_impl.storage.CrawledUrlStorage;
import org.example.utils.Validator;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Hello world!
 */
public class App {
    private static final Integer MAX_LEVEL = 5;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter URL: ");
        String url = scanner.nextLine();
        System.out.println("Enter depth: ");
        Integer depth = scanner.nextInt();
        System.out.println("Enter number of threads: ");
        int thread_count = scanner.nextInt();
        if (!Validator.validateUrl(url)) {
            System.out.println("Invalid URL");
            return;
        }
        if (depth > MAX_LEVEL) {
            System.out.println("Depth exceeds 5");
            return;
        }
        CrawlResult initial = new CrawlResult(url, depth);
        ExecutorService executorService = Executors.newFixedThreadPool(thread_count);
        CrawledUrlStorage crawledUrlStorage = new CrawledUrlStorage();
        Scheduler scheduler = new Scheduler(executorService, crawledUrlStorage);
        scheduler.addInitialUrl(initial);
        scheduler.start();
    }
}
