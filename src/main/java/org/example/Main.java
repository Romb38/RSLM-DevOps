package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.csv.CsvReadOptions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

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
            String dataframe = scanner.nextLine();
            Table table;
            if (isFile) {
                CsvReadOptions.Builder builder = CsvReadOptions.builder(dataframe)
                        .separator(';')
                        .header(true);
                table = Table.read().csv(builder.build());
            } else {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                out.write(dataframe.getBytes());
                table = Table.read().csv(new ByteArrayInputStream(out.toByteArray()));
            }
//            HashMap<String, Object[]> values = objectMapper.readValue(dataframe, typeRef);
            System.out.println("Result: ");
//            System.out.println(inputMapToString(values));
        } catch (Exception ex) {
            System.out.println("Wrong input");
        }
        scanner.close();
    }

    private static HashMap<String, Object[]> processManualInput(Scanner scanner) throws JsonProcessingException {
        boolean done = false;
        HashMap<String, Object[]> table = new HashMap<>();
        List<String> columns = new ArrayList<>();
        List<Object[]> rows = new ArrayList<>();
        while (!done) {
            System.out.println("Id of the column:");
            columns.add(scanner.nextLine());
            System.out.println("Add another column? (y = yes/anything else = no):");
            done = scanner.nextLine().equalsIgnoreCase("y");
        }
        done = false;
        ObjectMapper objectMapper = new ObjectMapper();
        while (!done) {
            System.out.println("Row value (max size=" + columns.size() + "):");
            String input = scanner.nextLine();
            Object[] value = objectMapper.readValue(input, Object[].class);
            if (value.length != columns.size()) {
                System.out.println("Wrong input (" + value.length + " elements instead of " + columns.size() + " expected)");
                done = false;
            } else {
                rows.add(value);
                System.out.println("Add another row? (y = yes/anything else = no):");
                done = scanner.nextLine().equalsIgnoreCase("y");
            }
        }
        List<Object> row;
        for (int i = 0; i < columns.size(); i++) {
            row = new ArrayList<>();
            for (int iRow = 0; iRow < rows.size(); iRow++) {
                row.add(rows.get(iRow)[i]);
            }
            table.put(columns.get(i), row.toArray());
        }
        return table;
    }

    // Print the whole table
    // Example: {"a":[1,5,6],"b":[9,3,2,4]}
    private static String inputMapToString(HashMap<String, Object[]> inputArray) {
        StringBuilder builder = new StringBuilder();
        int size = inputArray.keySet().size();
        buildTableDelimiter(builder, size);
        builder.append("|");
        for (String inputKey : inputArray.keySet()) {
            builder.append("\t").append(inputKey).append("\t|");
        }
        builder.append(System.lineSeparator());
        buildTableDelimiter(builder, size);
        for (int i = 0; i < inputArray.values().stream()
                .max(Comparator.comparing(Array::getLength))
                .map(Array::getLength).orElse(0); i++) {
            builder.append("|");
            for (String inputKey : inputArray.keySet()) {
                builder.append("\t").append(inputArray.get(inputKey).length > i ?
                        inputArray.get(inputKey)[i].toString() : " ").append("\t|");
            }
            builder.append(System.lineSeparator());
            buildTableDelimiter(builder, size);
        }
        return builder.toString();
    }

    private static void buildTableDelimiter(StringBuilder builder, int size) {
        for (int i = 0; i < size; i++) {
            builder.append("--------");
        }
        builder.append(System.lineSeparator());
    }
}