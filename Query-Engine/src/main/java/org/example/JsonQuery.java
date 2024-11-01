package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class JsonQuery implements JsonQueryProvider{
    private  final JSONTokener tokener;
    private final JSONObject data ;

    public JsonQuery(String jsonFilePath) throws FileNotFoundException {
        FileReader reader = new FileReader(jsonFilePath);
        tokener = new JSONTokener(reader);
        data = new JSONObject(tokener);

    }

    @Override
    public String searchInJsonDatamart(String word) {
        try  {
            if (data.has(word)) {
                JSONArray results = data.getJSONArray(word);
                StringBuilder output = new StringBuilder();

                for (int i = 0; i < results.length(); i++) {
                    JSONObject book = results.getJSONObject(i);
                    output.append("\nTitle: ").append(book.optString("title", "Unknown")).append("\n");
                    output.append("Author: ").append(book.optString("author", "Unknown")).append("\n");
                    output.append("Language: ").append(book.optString("language", "Unknown")).append("\n");
                    output.append("Released Date: ").append(book.optString("release_date", "Unknown")).append("\n");
                    output.append("-".repeat(40)).append("\n");
                }
                return output.toString();
            } else {
                return "No results were found for the word '" + word + "' in the JSON datamart.";
            }

        } catch (Exception e) {
            return "An error occurred: " + e.getMessage();
        }
    }
}

