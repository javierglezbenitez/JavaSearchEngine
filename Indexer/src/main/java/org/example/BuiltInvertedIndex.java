package org.example;

import org.bson.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class BuiltInvertedIndex implements InvertedIndexBuilder {


    MetadataExtraction metadataExtraction = new MetadataExtraction();

    private static final List<String> stopWords = Arrays.asList(
            "the", "and", "is", "in", "it", "of", "to", "a", "that", "with", "for", "as",
            "on", "was", "at", "by", "an", "be", "this", "which", "or", "from", "but",
            "not", "are", "have", "has", "had", "were", "they", "them", "their", "you",
            "yours", "us", "our"
    );

    @Override
    public Map<String, List<Document>> builtInvertedIndexMongo(String datalake) {
        Map<String, List<Document>> invertedDict = new HashMap<>();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(datalake), "book_*.txt")) {
            for (Path path : directoryStream) {
                try (BufferedReader reader = Files.newBufferedReader(path)) {
                    String line;
                    int lineNumber = 0; // Contador de líneas
                    Map<String, String> metadata = metadataExtraction.extractMetadata(Files.readString(path));

                    while ((line = reader.readLine()) != null) {
                        lineNumber++; // Incrementar número de línea
                        String[] words = line.toLowerCase().split("\\W+");

                        for (String word : words) {
                            if (!stopWords.contains(word) && !word.matches("\\d+") && !word.isEmpty() && Character.isLetter(word.charAt(0))) {
                                invertedDict.putIfAbsent(word, new ArrayList<>());

                                Document wordEntry = new Document(metadata)
                                        .append("line_number", lineNumber)
                                        .append("line_text", line.trim());

                                List<Document> wordList = invertedDict.get(word);
                                if (!wordList.contains(wordEntry)) {
                                    wordList.add(wordEntry);
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error processing file " + path.getFileName() + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading datalake path: " + e.getMessage());
        }

        return invertedDict;
    }


    @Override
    public Map<String, List<Map<String, String>>> builtInvertedIndexJson(String datalake) {
        Map<String, List<Map<String, String>>> invertedIndex = new HashMap<>();
        File folder = new File(datalake);

        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.getName().startsWith("book_") && file.getName().endsWith(".txt")) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    int lineNumber = 0; // Contador de líneas
                    Map<String, String> metadata = metadataExtraction.extractMetadata(Files.readString(file.toPath()));

                    while ((line = reader.readLine()) != null) {
                        lineNumber++; // Incrementar número de línea

                        String[] words = line.toLowerCase().split("\\W+");
                        for (String word : words) {
                            if (!stopWords.contains(word) && word.matches("^[a-zA-Z].*") && word.length() > 1) {
                                invertedIndex.putIfAbsent(word, new ArrayList<>());

                                // Crear un mapa que contenga la línea y el texto de la línea junto con los metadatos
                                Map<String, String> entry = new HashMap<>(metadata); // Copiar metadatos
                                entry.put("line_number", String.valueOf(lineNumber)); // Almacenar el número de línea
                                entry.put("line_text", line.trim()); // Almacenar el texto completo de la línea

                                // Agregar la información de la línea y los metadatos al índice invertido
                                List<Map<String, String>> metadataList = invertedIndex.get(word);
                                if (!metadataList.contains(entry)) {
                                    metadataList.add(entry);
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error reading file: " + file.getName());
                    e.printStackTrace();
                }
            }
        }
        return invertedIndex;
    }
}