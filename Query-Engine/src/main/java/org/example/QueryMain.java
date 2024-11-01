package org.example;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class QueryMain {

    private static String url = "mongodb://localhost:27017";
    private static String jsonFilePath = "C:\\Users\\cgsos\\Documents\\Tercero\\Big Data\\JavaSearchEngine\\SearchEngine\\jsonDatamart\\jsonDatamartFile.json";

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Type 'consult' to look up a word or 'exit' to end the program:\n");
            String command = scanner.nextLine();


            if (command.equals("consult")){

                System.out.println("What word do you want to look up? ");
                String word = scanner.nextLine();

                JsonQueryProvider jsonQueryProvider = new JsonQuery(jsonFilePath);
                String output = jsonQueryProvider.searchInJsonDatamart(word);
                System.out.println(output);
            } else if (command.equals("exit")) {
                System.out.println("Closing the program...");
                break; // Termina el bucle si el usuario escribe "salir"

            }
            else {
                System.out.println("Commmand not recognized");
            }

        }
        scanner.close();
    }
}
