package org.example;

import com.mongodb.client.*;
import org.bson.Document;

public class MongoQuery implements MongoQueryProvider {

    private final String dbName = "BooksDatabase";
    private final String collectionName = "InvertedIndex";
    private final MongoCollection<Document> collection;




    public MongoQuery(String url){
        MongoClient mongoClient = MongoClients.create(url);
        MongoDatabase db = mongoClient.getDatabase(dbName);
        collection = db.getCollection(collectionName);

    }


    @Override
    public  String searchInMongoDatamart(String word) {
        try {
            word = word.toLowerCase().trim();

            Document query = new Document("_id", word);
            MongoCursor<Document> cursor = collection.find(query).iterator();

            if (!cursor.hasNext()) {
                return "No results were found for the word '" + word + "' in Mongo Database.";
            }

            StringBuilder output = new StringBuilder();
            while (cursor.hasNext()) {
                Document result = cursor.next();
                for (Document book : (Iterable<Document>) result.get("Books")) {
                    output.append("\nTitle: ").append(book.getString("title") != null ? book.getString("title") : "Unknown").append("\n");
                    output.append("Author: ").append(book.getString("author") != null ? book.getString("author") : "Unknown").append("\n");
                    output.append("Language: ").append(book.getString("language") != null ? book.getString("language") : "Unknown").append("\n");
                    output.append("Released Date: ").append(book.getString("release_date") != null ? book.getString("release_date") : "Unknown").append("\n");
                    output.append("-".repeat(40)).append("\n");
                }
            }

            cursor.close();
            return output.toString().trim();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

