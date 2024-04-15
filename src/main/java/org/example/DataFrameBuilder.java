package org.example;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataFrameBuilder {

    /**
     * Read CSV file for a given file path
     * Note: csv delimiter character is `;`
     *
     * @param path file path
     * @return Dataframe extracted
     * @throws IOException If unable to open the file
     */
    public static DataFrame buildDataFrameFromInput(Path path) throws IOException {
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = CsvSchema.emptySchema().withHeader();

        MappingIterator<Map<String, Object[]>> mappingIterator = csvMapper.readerFor(Map.class).with(csvSchema).with(csvSchema.withColumnSeparator(';')).readValues(path.toFile());
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
        return buildDataFrame(columns, rows);
    }

    /**
     * Build a DataFrame from columns and rows
     *
     * @param columns list of columns to declare
     * @param rows    list of rows to declare
     * @return Dataframe extracted
     */
    public static DataFrame buildDataFrame(List<String> columns, List<Object[]> rows) {
        HashMap<String, Object[]> table = new HashMap<>();
        List<Object> row;
        for (int i = 0; i < columns.size(); i++) {
            row = new ArrayList<>();
            for (Object[] objects : rows) {
                row.add(objects[i]);
            }
            table.put(columns.get(i), row.toArray());
        }
        return new DataFrame(table);
    }
}
