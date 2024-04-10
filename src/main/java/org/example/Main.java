package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    /**
     * @brief Main function
     * @details This method is the entry point of the program
     */
    public static void main(String[] args) throws IOException {
        boolean validInput = false;
        boolean isFile = false;
        Scanner scanner = new Scanner(System.in);
        while (!validInput) {
            System.out.println("File or input? (f/i): ");
            String result = scanner.nextLine();
            if (result.equalsIgnoreCase("i") || result.equalsIgnoreCase("f")) {
                isFile = result.contains("f");
                validInput = true;
            } else {
                System.out.println("Wrong input, please type 'f' or 'i'");
            }
        }
        try {
            // WORK IN PROGRESS
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap<String, Object[]> table;
            if (isFile) {
                table = DataFrameReader.processFileInput(scanner);
            } else {
                table = DataFrameReader.processManualInput(scanner, objectMapper);
            }
            System.out.println("Result: ");
            System.out.println(DataFrameUtils.inputMapToString(table));
        } catch (Exception ex) {
            System.out.println("Wrong input: " + ex);
        }
        scanner.close();
    }
}