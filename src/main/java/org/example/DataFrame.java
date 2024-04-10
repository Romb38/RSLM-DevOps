package org.example;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class DataFrame<H, T> {
    private final Class<T> classParameterT;
    private final Class<H> classParameterH;
    private Integer[] index;
    private HashMap<H, List<T>> content = new HashMap<>();


    /**
     * Read CSV file for a given file path
     * Note: csv delimiter character is `;`
     *
     * @param path file path
     * @throws IOException If unable to open the file
     */
    DataFrame(Path path, Character columnSeparator, Class<H> classParameterH, Class<T> classParameterT) throws IOException {
        this.classParameterT = classParameterT;
        this.classParameterH = classParameterH;
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = CsvSchema.emptySchema().withHeader();
        if (columnSeparator == null) {
            columnSeparator = ';';
        }

        MappingIterator<Map<String, String>> mappingIterator = csvMapper.readerFor(Map.class)
                .with(csvSchema)
                .with(csvSchema.withColumnSeparator(columnSeparator))
                .readValues(path.toFile());
        List<List<T>> rows = new ArrayList<>();
        List<H> header = new ArrayList<>();

        while (mappingIterator.hasNext()) {
            Map<String, String> row = mappingIterator.next();
            List<T> values = row.values().stream()
                    .map(this::convertValueT)
                    .collect(Collectors.toList());
            rows.add(values);
            if (header.isEmpty()) {
                header.addAll(row.keySet().stream()
                        .map(this::convertValueH)
                        .toList());
            }
        }
        HashMap<H, List<T>> table = new HashMap<>();
        List<T> row;
        for (int i = 0; i < header.size(); i++) {
            row = new ArrayList<>();
            for (List<T> objects : rows) {
                row.add(objects.get(i));
            }
            table.put(header.get(i), row);
        }
        this.setContent(table);
    }

    /**
     * Build a DataFrame from header and rows
     *
     * @param header list of header to declare
     * @param rows   list of rows to declare
     */
    DataFrame(List<H> header, List<List<T>> rows, Class<H> classParameterH, Class<T> classParameterT) {
        this.classParameterT = classParameterT;
        this.classParameterH = classParameterH;
        HashMap<H, List<T>> table = new HashMap<>();
        List<T> row;
        for (int i = 0; i < header.size(); i++) {
            row = new ArrayList<>();
            for (List<T> objects : rows) {
                row.add(objects.get(i));
            }
            table.put(header.get(i), row);
        }
        this.setContent(table);
    }

    DataFrame(HashMap<H, List<T>> content, Integer[] index, Class<H> classParameterH, Class<T> classParameterT) {
        this.classParameterT = classParameterT;
        this.classParameterH = classParameterH;
        this.setContent(content);
        this.setIndex(index);
    }

    DataFrame(HashMap<H, List<T>> content, Class<H> classParameterH, Class<T> classParameterT) {
        this.classParameterT = classParameterT;
        this.classParameterH = classParameterH;
        this.setContent(content);
    }

    public Integer[] getIndex() {
        return index;
    }

    public void setIndex(Integer[] index) {
        this.index = index;
    }

    public HashMap<H, List<T>> getContent() {
        return content;
    }

    public void setContent(HashMap<H, List<T>> content) {
        this.content = content;
    }

    /**
     * Display in a table the given dataframe
     * Example of result:
     * ------------------
     * |    a   |   b   |
     * ------------------
     * |    1   |   2   |
     * ------------------
     * |    5   |   9   |
     * ------------------
     *
     * @return Result to display
     */
    public String toStringDisplay() {
        HashMap<H, List<T>> inputArray = this.getContent();
        StringBuilder builder = new StringBuilder();
        int size = inputArray.keySet().size();
        buildTableDelimiter(builder, size);
        builder.append("|");
        for (H inputKey : inputArray.keySet()) {
            builder.append("\t").append(inputKey).append("\t|");
        }
        builder.append(System.lineSeparator());
        buildTableDelimiter(builder, size);
        for (int i = 0; i < inputArray.values().stream()
                .max(Comparator.comparing(List::size))
                .map(List::size).orElse(0); i++) {
            builder.append("|");
            for (H inputKey : inputArray.keySet()) {
                builder.append("\t").append(inputArray.get(inputKey).size() > i ?
                        inputArray.get(inputKey).get(i).toString() : " ").append("\t|");
            }
            builder.append(System.lineSeparator());
            buildTableDelimiter(builder, size);
        }
        return builder.toString();
    }

    /**
     * Add delimitation between each row
     *
     * @param builder String builder of the current table display
     * @param size    number of spaces to add
     */
    private void buildTableDelimiter(StringBuilder builder, int size) {
        builder.append("--------".repeat(Math.max(0, size)));
        builder.append(System.lineSeparator());
    }

    @SuppressWarnings("unchecked")
    private T convertValueT(String input) {
        if (classParameterT.isAssignableFrom(String.class)) {
            return (T) input;
        } else if (classParameterT.isAssignableFrom(Integer.class)) {
            return (T) Integer.valueOf(input);
        } else if (classParameterT.isAssignableFrom(Boolean.class)) {
            return (T) Boolean.valueOf(input);
        } else if (classParameterT.isAssignableFrom(Double.class)) {
            return (T) Double.valueOf(input);
        } else {
            throw new IllegalArgumentException("Bad type.");
        }
    }

    @SuppressWarnings("unchecked")
    private H convertValueH(String input) {
        if (classParameterH.isAssignableFrom(String.class)) {
            return (H) input;
        } else if (classParameterH.isAssignableFrom(Integer.class)) {
            return (H) Integer.valueOf(input);
        } else if (classParameterH.isAssignableFrom(Boolean.class)) {
            return (H) Boolean.valueOf(input);
        } else if (classParameterH.isAssignableFrom(Double.class)) {
            return (H) Double.valueOf(input);
        } else {
            throw new IllegalArgumentException("Bad type.");
        }
    }
}
