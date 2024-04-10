package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {

    /**
     * @brief Méthode principale
     *
     * @details This method is the entry point of the program
     */
    public static void main(String[] args) throws IOException {
        boolean validInput = false;
        boolean isFile = false;
        try {
            // WORK IN PROGRESS
            String csvFilePath = System.getenv("CSV_FILE_PATH");
            DataFrame dataFrame = DataFrameBuilder.buildDataFrameFromInput(Paths.get(csvFilePath));
            System.out.println("Result: ");
            System.out.println(DataFrameUtils.inputMapToString(dataFrame));
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}