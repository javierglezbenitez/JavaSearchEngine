package org.example;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CrawlerMain {
    private  static String datalakePath = "C:\\Users\\cgsos\\Documents\\Tercero\\Big Data\\JavaSearchEngine\\SearchEngine";
    public static void main(String[] args) {
        Crawler crawler = new Crawler(datalakePath);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
        crawler.crawlerRunner(); // Llamar al método crawler() del objeto Crawler
    }, 2, 20, TimeUnit.SECONDS);


}
}
