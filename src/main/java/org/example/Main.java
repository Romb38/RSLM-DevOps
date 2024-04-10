package org.example;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {

    /**
     * @brief Méthode principale
     *
     * @details This method is the entry point of the program
     */
    public static void main(String[] args) throws IOException {
        try {
            // WORK IN PROGRESS
//            String csvFilePath = System.getenv("CSV_FILE_PATH");
            DataFrame<String, Integer> dataFrame = new DataFrame<>(Paths.get("C:\\Users\\Jack Asmens\\Documents\\0. Important\\Cours_M1\\input.csv"), ';', String.class, Integer.class);
            System.out.println("Result: ");
            System.out.println(dataFrame.toStringDisplay());
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}