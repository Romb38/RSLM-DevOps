package org.example;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {

    /**
     * @brief Main function
     * @details This method is the entry point of the program
     */
    public static void main(String[] args) {
        try {
            // Vérifie si un argument (chemin du fichier CSV) est fourni
            if (args.length > 0) {
                String csvFilePath = args[0];
                DataFrame dataFrame = DataFrameBuilder.buildDataFrameFromInput(Paths.get(csvFilePath));
                System.out.println("Result: ");
                System.out.println(DataFrameUtils.inputMapToString(dataFrame));
            } else {
                System.out.println("Please provide the CSV file path as a command line argument.");
            }
        } catch (Exception ex) {
            System.out.println("An error occurred: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
