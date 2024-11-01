package org.example;

import org.bson.json.JsonParseException;

public interface JsonQueryProvider {
    String searchInJsonDatamart(String word) throws JsonParseException;
}
