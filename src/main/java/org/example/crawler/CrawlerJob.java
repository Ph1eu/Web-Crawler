package org.example.crawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class CrawlerJob {
    public void crawl(String url, Integer level) throws IOException {
        if (level <= 0) {
            return; // Stop crawling when depth reaches zero
        }
        Document document = request(url);
//        String title = document.title();
//        System.out.println("Title: " + title);

        assert document != null;
        Elements links = document.select("a[href]");
        System.out.println("Received web page at " + url);
        for (Element link : links) {
            String nextUrl = link.attr("abs:href");
            // System.out.printf("Next URL: %s\n", nextUrl);
            crawl(nextUrl, level - 1);
        }
    }
 private static Document request(String url) throws IOException {
    Connection connection = Jsoup.connect(url);
    Document document = connection.get();
    if(connection.response().statusCode() == 200){
        System.out.println("Received web page at " + url);
        System.out.println("Title: " + document.title());
        return document;
    } else {
        throw new IOException("HTTP status code: " + connection.response().statusCode());
    }
}

}
