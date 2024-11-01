package org.example;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class IndexerMain {
    private static final String datalakePath = "C:\\Users\\cgsos\\Documents\\Tercero\\Big Data\\JavaSearchEngine\\SearchEngine\\Datalake";
    private static final String jsonDatamartPath = "C:\\Users\\cgsos\\Documents\\Tercero\\Big Data\\JavaSearchEngine\\SearchEngine\\";

    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        InvertedIndexBuilder invertedIndexBuilder = new BuiltInvertedIndex();
        InvertedIndexStorer invertedIndexStorer = new StoreInvertedIndex();

        scheduler.scheduleAtFixedRate(() -> {
            invertedIndexStorer.storeInvertedIndexJson(invertedIndexBuilder.builtInvertedIndexJson(datalakePath),jsonDatamartPath);
            System.out.println("Los nuevos libros se han indexado correctamente en el JSON DATAMART");

            invertedIndexStorer.storeInvertedIndexMongo(invertedIndexBuilder.builtInvertedIndexMongo(datalakePath));
            System.out.println("Los nuevos libros se han indexado correctamente en el Mongo DATAMART");
        }, 0, 25, TimeUnit.SECONDS); // Ejecuta cada 20 segundos

    }

}
