package org.example;

import com.mongodb.MongoException;

public interface MongoQueryProvider {

    String searchInMongoDatamart(String word) throws MongoException;

}
