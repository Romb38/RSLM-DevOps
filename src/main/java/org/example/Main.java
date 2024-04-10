package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Paths;
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
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap<String, Object[]> table;
            if (isFile) {
                table = processFileInput(scanner);
            } else {
                table = processManualInput(scanner, objectMapper);
            }
            System.out.println("Result: ");
            System.out.println(inputMapToString(table));
        } catch (Exception ex) {
            System.out.println("Wrong input: " + ex);
        }
        scanner.close();
    }

    private static HashMap<String, Object[]> processFileInput(Scanner scanner) throws IOException {
        HashMap<String, Object[]> table;
        System.out.println("File path: ");
        String input = scanner.nextLine();
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = CsvSchema.emptySchema().withHeader();

        MappingIterator<Map<String, Object[]>> mappingIterator = csvMapper
                .readerFor(Map.class)
                .with(csvSchema)
                .with(csvSchema.withColumnSeparator(';'))
                .readValues(Paths.get(input).toFile());

        table = new HashMap<>();
        List<Object[]> rows = new ArrayList<>();
        List<String> columns = new ArrayList<>();

        while (mappingIterator.hasNext()) {
            Map<String, Object[]> row = mappingIterator.next();
            Object[] values = row.values().toArray();
            rows.add(values);
            if (columns.isEmpty()) {
                columns.addAll(row.keySet());
            }
        }
        List<Object> row;
        for (int i = 0; i < columns.size(); i++) {
            row = new ArrayList<>();
            for (Object[] objects : rows) {
                row.add(objects[i]);
            }
            table.put(columns.get(i), row.toArray());
        }
        return table;
    }

    private static HashMap<String, Object[]> processManualInput(Scanner scanner, ObjectMapper objectMapper) throws JsonProcessingException {
        boolean done = false;
        HashMap<String, Object[]> table = new HashMap<>();
        List<String> columns = new ArrayList<>();
        List<Object[]> rows = new ArrayList<>();
        while (!done) {
            System.out.println("Id of the column:");
            columns.add(scanner.nextLine());
            System.out.println("Add another column? (y = yes/anything else = no):");
            done = !scanner.nextLine().equalsIgnoreCase("y");
        }
        done = false;
        while (!done) {
            System.out.println("Row value (max size=" + columns.size() + "):");
            String input = scanner.nextLine();
            Object[] value = objectMapper.readValue(input, Object[].class);
            if (value.length != columns.size()) {
                System.out.println("Wrong input (" + value.length + " elements instead of " + columns.size() + " expected)");
            } else {
                rows.add(value);
                System.out.println("Add another row? (y = yes/anything else = no):");
                done = !scanner.nextLine().equalsIgnoreCase("y");
            }
        }
        List<Object> row;
        for (int i = 0; i < columns.size(); i++) {
            row = new ArrayList<>();
            for (Object[] objects : rows) {
                row.add(objects[i]);
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
        builder.append("--------".repeat(Math.max(0, size)));
        builder.append(System.lineSeparator());

     * @brief Méthode principale améliorée
     *
     * @details This method is the entry point of the program
     */
    public static void main(String[] args) {
        System.out.println("Hello world! And root");
        System.out.println("yo");

    }
}