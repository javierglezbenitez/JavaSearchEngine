package org.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Crawler {
    private static int currentBookId = 1; // Comenzar en 1, ya que 0 no tiene un libro
    private TitleExtractor titleExtractor;
    private String datalakePath;

    // Constructor
    public Crawler(String datalakePath) {
        this.titleExtractor = new TitleExtractor(); // Inicializar el objeto TitleExtractor
        this.datalakePath = datalakePath;
    }

    // Método crawler no estático
    public void crawlerRunner() {

        File outputfile = new File(datalakePath + File.separator + "Datalake");

        if (!outputfile.exists()) {
            outputfile.mkdirs();
        }

        int downloadedBook = 0;
        List<String> downloadedTitles = new ArrayList<>(); // Lista para almacenar los títulos descargados

        while (downloadedBook < 4) { // Cambia esto si deseas descargar más libros
            String urlString = String.format("https://www.gutenberg.org/cache/epub/%d/pg%d.txt", currentBookId, currentBookId);
            try {
                String response = getResponseFromUrl(urlString);
                String title = titleExtractor.readContent(response, 40); // Llamada al método usando el objeto

                if (title != null) {
                    String fileName = String.format("book_%s.txt", title.replaceAll("[\\\\/:*?\"<>|]", "_")); // Reemplazar caracteres inválidos
                    String fullPath = String.format("%s\\%s", outputfile, fileName);

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath, false))) {
                        writer.write(response);
                    }

                    downloadedTitles.add(title); // Agregar título a la lista
                    downloadedBook++;
                }
            } catch (IOException e) {
                System.err.println("Error al descargar el libro con ID " + currentBookId + ": " + e.getMessage());
            }

            currentBookId++; // Incrementar el ID del libro para el siguiente ciclo
        }

        // Imprimir los títulos descargados
        System.out.println("Libros descargados:");
        for (String title : downloadedTitles) {
            System.out.println(title);
        }
    }

    private static String getResponseFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Verificar si el código de respuesta es HTTP_OK
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (Scanner scanner = new Scanner(connection.getInputStream())) {
                StringBuilder content = new StringBuilder();
                while (scanner.hasNextLine()) {
                    content.append(scanner.nextLine()).append(System.lineSeparator());
                }
                return content.toString();
            }
        } else {
            throw new IOException("Failed to get response from the URL: " + urlString + " (HTTP status: " + connection.getResponseCode() + ")");
        }
    }
}
