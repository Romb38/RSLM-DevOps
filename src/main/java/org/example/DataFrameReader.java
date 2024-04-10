package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class DataFrameReader {

    /**
     * Read CSV file for a given file path
     * Note: csv delimiter character is `;`
     *
     * @param scanner Console scanner used to interact
     * @return Dataframe extracted
     * @throws IOException If unable to open the file
     */
    public static HashMap<String, Object[]> processFileInput(Scanner scanner) throws IOException {
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

    /**
     * Read Manual input to build a DataFrame
     *
     * @param scanner      Console scanner used to interact
     * @param objectMapper Jackson object mapper
     * @return Dataframe extracted
     * @throws JsonProcessingException If unable to map input value
     */
    public static HashMap<String, Object[]> processManualInput(Scanner scanner, ObjectMapper objectMapper) throws JsonProcessingException {
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
}
