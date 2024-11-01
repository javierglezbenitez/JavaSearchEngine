package org.example;

import com.mongodb.MongoException;
import org.bson.Document;
import org.bson.json.JsonParseException;

import java.util.List;
import java.util.Map;

public interface InvertedIndexBuilder {
    Map<String, List<Map<String, String>>> builtInvertedIndexJson(String datalake) throws JsonParseException;
    Map<String, List<Document>> builtInvertedIndexMongo(String datalakle)throws MongoException;
}
