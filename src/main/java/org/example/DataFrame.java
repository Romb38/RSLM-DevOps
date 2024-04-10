package org.example;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.util.*;

public class DataFrame<H, T> {
    private Object index;
    private HashMap<H, T[]> content = new HashMap<>();


    /**
     * Read CSV file for a given file path
     * Note: csv delimiter character is `;`
     *
     * @param path file path
     * @return Dataframe extracted
     * @throws IOException If unable to open the file
     */
    DataFrame(Path path, Character columnSeparator) throws IOException {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = CsvSchema.emptySchema().withHeader();
        if (columnSeparator == null) {
            columnSeparator = ';';
        }

        MappingIterator<Map<H, T[]>> mappingIterator = csvMapper.readerFor(Map.class)
                .with(csvSchema)
                .with(csvSchema.withColumnSeparator(columnSeparator))
                .readValues(path.toFile());
        List<T[]> rows = new ArrayList<>();
        List<H> columns = new ArrayList<>();

        while (mappingIterator.hasNext()) {
            Map<H, T[]> row = mappingIterator.next();
            T[] values = (T[]) row.values().toArray();
            rows.add(values);
            if (columns.isEmpty()) {
                columns.addAll(row.keySet());
            }
        }
        return buildDataFrame(columns, rows);
    }

    /**
     * Build a DataFrame from header and rows
     *
     * @param header list of header to declare
     * @param rows   list of rows to declare
     * @return Dataframe extracted
     */
    DataFrame(List<H> header, List<T[]> rows) {
        HashMap<H, T[]> table = new HashMap<>();
        List<T> row;
        for (int i = 0; i < header.size(); i++) {
            row = new ArrayList<>();
            for (T[] objects : rows) {
                row.add(objects[i]);
            }
            table.put(header.get(i), row.toArray());
        }
        this.setContent(table);
    }

    DataFrame(HashMap<H, T[]> content, Object index) {
        this.setContent(content);
        this.setIndex(index);
    }

    DataFrame(HashMap<H, T[]> content) {
        this.setContent(content);
    }

    /**
     * Add delimitation between each row
     *
     * @param builder String builder of the current table display
     * @param size    number of spaces to add
     */
    private static void buildTableDelimiter(StringBuilder builder, int size) {
        builder.append("--------".repeat(Math.max(0, size)));
        builder.append(System.lineSeparator());
    }

    public Object getIndex() {
        return index;
    }

    public void setIndex(Object index) {
        this.index = index;
    }

    public HashMap<H, T[]> getContent() {
        return content;
    }

    public void setContent(HashMap<H, T[]> content) {
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
        HashMap<H, T[]> inputArray = this.getContent();
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
                .max(Comparator.comparing(Array::getLength))
                .map(Array::getLength).orElse(0); i++) {
            builder.append("|");
            for (H inputKey : inputArray.keySet()) {
                builder.append("\t").append(inputArray.get(inputKey).length > i ?
                        inputArray.get(inputKey)[i].toString() : " ").append("\t|");
            }
            builder.append(System.lineSeparator());
            buildTableDelimiter(builder, size);
        }
        return builder.toString();
    }
}
