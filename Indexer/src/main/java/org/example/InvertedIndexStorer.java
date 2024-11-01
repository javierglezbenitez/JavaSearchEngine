package org.example;

import com.mongodb.MongoException;
import org.bson.Document;

import java.util.List;
import java.util.Map;

public interface InvertedIndexStorer {
    void storeInvertedIndexMongo(Map<String, List<Document>> invertedDict)throws MongoException;
    void storeInvertedIndexJson(Map<String, List<Map<String, String>>> invertedIndex, String outputFile)throws MongoException;
}
