package org.example;
import org.example.crawler.CrawlResult;
import org.example.crawler.CrawlerJob;
import org.example.scheduler.Scheduler;
import org.example.storage.CrawledUrlStorage;
import org.example.utils.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;
import java.util.concurrent.*;


/**
 * Hello world!
 *
 */
public class App 
{
    private final Integer MAX_LEVEL =  5;
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    public static void main( String[] args )
    {
        // provide input cli
        // validate input
        // call crawler
        // print output
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter URL: ");
        String url = scanner.nextLine();
        System.out.println("Enter depth: ");
        Integer depth = scanner.nextInt();
        if(!Validator.validateUrl(url)){
            System.out.println("Invalid URL");
            return;
        }
        if(depth > 5){
            System.out.println("Depth exceeds 5");
            return;
        }
        CrawlResult initial = new CrawlResult(url,depth);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CrawledUrlStorage crawledUrlStorage = new CrawledUrlStorage();
        Scheduler scheduler = new Scheduler(executorService,crawledUrlStorage);
        scheduler.addInitialUrl(initial);
        scheduler.start();
        executorService.shutdown();
    }
}
