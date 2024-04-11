package org.example;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class DataFrame<H, T> {
    private final Class<T> classParameterT;
    private final Class<H> classParameterH;
    private Integer[] indexes;
    private HashMap<H, List<T>> content = new HashMap<>();


    /**
     * Read CSV file for a given file path
     * Note: csv default delimiter character is `;`
     *
     * @param path file path
     * @throws IOException If unable to open the file
     */
    public DataFrame(Path path, Character csvDelimiter, Class<H> classParameterH, Class<T> classParameterT) throws IOException {
        this.classParameterT = classParameterT;
        this.classParameterH = classParameterH;
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema csvSchema = CsvSchema.emptySchema().withHeader();
        if (csvDelimiter == null) {
            csvDelimiter = ';';
        }

        MappingIterator<Map<String, String>> mappingIterator = csvMapper.readerFor(Map.class).with(csvSchema).with(csvSchema.withColumnSeparator(csvDelimiter)).readValues(path.toFile());
        List<List<T>> rows = new ArrayList<>();
        List<H> header = new ArrayList<>();

        while (mappingIterator.hasNext()) {
            Map<String, String> row = mappingIterator.next();
            List<T> values = row.values().stream().map(this::convertValueT).collect(Collectors.toList());
            rows.add(values);
            if (header.isEmpty()) {
                header.addAll(row.keySet().stream().map(this::convertValueH).toList());
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
     * Read CSV file for a given file path
     * Note: csv default delimiter character is `;`
     *
     * @param path file path
     * @throws IOException If unable to open the file
     */
    public DataFrame(Path path, Class<H> classParameterH, Class<T> classParameterT) throws IOException {
        this(path, ';', classParameterH, classParameterT);
    }

    /**
     * Build a DataFrame from header and rows
     *
     * @param header list of header to declare
     * @param rows   list of rows to declare
     */
    public DataFrame(List<H> header, List<List<T>> rows, Class<H> classParameterH, Class<T> classParameterT) {
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


    /**
     * Build a DataFrame from content Map and indexes
     *
     * @param content Data frame content shaped has a map
     * @param indexes Data frame indexes
     */
    public DataFrame(HashMap<H, List<T>> content, Integer[] indexes, Class<H> classParameterH, Class<T> classParameterT) {
        this.classParameterT = classParameterT;
        this.classParameterH = classParameterH;
        this.setContent(content);
        this.setIndexes(indexes);
    }

    /**
     * Build a DataFrame from content Map
     *
     * @param content Data frame content shaped has a map
     */
    public DataFrame(HashMap<H, List<T>> content, Class<H> classParameterH, Class<T> classParameterT) {
        this.classParameterT = classParameterT;
        this.classParameterH = classParameterH;
        this.setContent(content);
    }

    public Integer[] getIndexes() {
        return indexes;
    }

    public void setIndexes(Integer[] indexes) {
        this.indexes = indexes;
    }

    public HashMap<H, List<T>> getContent() {
        return content;
    }

    public void setContent(HashMap<H, List<T>> content) {
        this.content = content;
    }

    public DataFrame<H, T> selectDataFrameByIndex(Integer[] indexes) throws Exception {
        checkIndexInput(indexes);
        HashMap<H, List<T>> content = new HashMap<>(this.getContent().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, map -> {
                    List<T> list = new ArrayList<>();
                    for (int i = 0; i < map.getValue().size(); i++) {
                        Integer index = retrieveIndexWhenAtDataFramePosition(indexes, i);
                        if (index != null)
                            list.add(map.getValue().get(i));
                    }
                    return list;
                })));
        return new DataFrame<>(content, indexes, classParameterH, classParameterT);
    }

    public DataFrame<H, T> selectDataFrameByColumn(List<H> columns) throws Exception {
        checkHeaderInput(columns);
        HashMap<H, List<T>> content = new HashMap<>(this.getContent().entrySet().stream()
                .filter(entry -> columns.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        if (this.getIndexes() != null && this.getIndexes().length != 0)
            return new DataFrame<>(content, indexes, classParameterH, classParameterT);
        else
            return new DataFrame<>(content, classParameterH, classParameterT);

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
     * @return String result to display
     */
    public String toStringDisplay() throws Exception {
        if (this.getIndexes() != null && this.getIndexes().length != 0) {
            return toStringDisplayByIndex(this.getIndexes());
        } else {
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
            for (int i = 0; i < inputArray.values().stream().max(Comparator.comparing(List::size)).map(List::size).orElse(0); i++) {
                builder.append("|");
                for (H inputKey : inputArray.keySet()) {
                    builder.append("\t").append(inputArray.get(inputKey).size() > i ? inputArray.get(inputKey).get(i).toString() : " ").append("\t|");
                }
                builder.append(System.lineSeparator());
                buildTableDelimiter(builder, size);
            }
            return builder.toString();
        }
    }

    /**
     * Display in a table the given dataframe for the given indexes
     * Example of result:
     * ------------------
     * |    a   |   b   |
     * ------------------
     * |    1   |   2   |
     * ------------------
     * |    5   |   9   |
     * ------------------
     *
     * @return String result to display
     */
    public String toStringDisplayByIndex(Integer[] indexes) throws Exception {
        checkIndexInput(indexes);
        HashMap<H, List<T>> inputArray = this.getContent();
        StringBuilder builder = new StringBuilder();

        // Size + 1 since we count the Index column too
        int size = inputArray.keySet().size() + 1;
        buildTableDelimiter(builder, size);
        builder.append("|\t \t|");
        for (H inputKey : inputArray.keySet()) {
            builder.append("\t").append(inputKey).append("\t|");
        }

        builder.append(System.lineSeparator());
        buildTableDelimiter(builder, size);
        Integer index;
        for (int i = 0; i < inputArray.values().stream().max(Comparator.comparing(List::size)).map(List::size).orElse(0); i++) {
            index = retrieveIndexWhenAtDataFramePosition(indexes, i);
            if (index != null) {
                builder.append("|\t").append(index).append("\t|");
                for (H inputKey : inputArray.keySet()) {
                    builder.append("\t").append(inputArray.get(inputKey).size() > i ? inputArray.get(inputKey).get(i).toString() : " ").append("\t|");
                }
                builder.append(System.lineSeparator());
                buildTableDelimiter(builder, size);
            }
        }
        return builder.toString();
    }

    /**
     * Display in a table the given dataframe for the given indexes
     * Example of result:
     * ------------------
     * |    a   |   b   |
     * ------------------
     * |    1   |   2   |
     * ------------------
     * |    5   |   9   |
     * ------------------
     *
     * @return String result to display
     */
    public String toStringDisplayByHeader(List<H> header) throws Exception {
        checkHeaderInput(header);
        HashMap<H, List<T>> inputArray = this.getContent();
        StringBuilder builder = new StringBuilder();

        // Size + 1 since we count the Index column too
        int size = hasIndex() ? header.size() + 1 : header.size();
        buildTableDelimiter(builder, size);
        builder.append("|");
        if (hasIndex())
            builder.append("\t \t|");
        for (H inputKey : header) {
            builder.append("\t").append(inputKey).append("\t|");
        }

        builder.append(System.lineSeparator());
        buildTableDelimiter(builder, size);
        Integer index;
        for (int i = 0; i < inputArray.values().stream().max(Comparator.comparing(List::size)).map(List::size).orElse(0); i++) {
            builder.append("|");
            if (hasIndex()) {
                index = this.getIndexes()[i];
                builder.append("\t").append(index).append("\t|");
            }
            for (H inputKey : header) {
                builder.append("\t").append(inputArray.get(inputKey).size() > i ? inputArray.get(inputKey).get(i).toString() : " ").append("\t|");
            }
            builder.append(System.lineSeparator());
            buildTableDelimiter(builder, size);
        }
        return builder.toString();
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
     * @param nbLines number of lines to display
     * @param mode    true = first n lines // false = last n lines
     * @return String result to display
     */
    public String toStringPartialDisplay(int nbLines, boolean mode) {
        if (nbLines < 0)
            nbLines = 0;
        HashMap<H, List<T>> inputArray = this.getContent();
        StringBuilder builder = new StringBuilder();

        // Size + 1 if we need to count the Index column
        int size = hasIndex() ? inputArray.keySet().size() + 1 : inputArray.keySet().size();
        buildTableDelimiter(builder, size);
        builder.append("|");
        if (hasIndex())
            builder.append("\t \t|");

        for (H inputKey : inputArray.keySet()) {
            builder.append("\t").append(inputKey).append("\t|");
        }

        builder.append(System.lineSeparator());
        buildTableDelimiter(builder, size);
        Integer index;
        int maxElement = mode ? nbLines : inputArray.values().stream().max(Comparator.comparing(List::size)).map(List::size).orElse(0);
        int initialValue = mode ? 0 : Math.max(0, inputArray.values().stream().max(Comparator.comparing(List::size)).map(List::size).orElse(0) - nbLines);
        for (int i = initialValue; i < maxElement; i++) {
            builder.append("|");
            if (hasIndex()) {
                index = this.getIndexes()[i];
                builder.append("\t").append(index).append("\t|");
            }

            for (H inputKey : inputArray.keySet()) {
                builder.append("\t").append(inputArray.get(inputKey).size() > i ? inputArray.get(inputKey).get(i).toString() : " ").append("\t|");
            }
            builder.append(System.lineSeparator());
            buildTableDelimiter(builder, size);
        }
        return builder.toString();
    }

    public boolean hasIndex() {
        return this.getIndexes() != null && this.getIndexes().length > 0;
    }

    private void checkIndexInput(Integer[] indexes) throws Exception {
        if (indexes == null) throw new Exception("Given index can't be null");
        if (indexes.length == 0) throw new Exception("Given index can't be empty");
        if (this.getIndexes() == null || this.getIndexes().length == 0)
            throw new Exception("No index have been configured yet");
        if (indexes.length > this.getIndexes().length)
            throw new Exception("Given index array exceed the current index array length (" + indexes.length + " instead of " + this.getIndexes().length + ")");
        for (Integer index : indexes) {
            if (Arrays.stream(this.getIndexes()).noneMatch(val -> Objects.equals(val, index)))
                throw new Exception("Not existing index given: " + index);
        }
    }

    private void checkHeaderInput(List<H> header) throws Exception {
        if (header == null) throw new Exception("Given header can't be null");
        if (header.isEmpty()) throw new Exception("Given header can't be empty");
        if (this.getContent() == null || this.getContent().keySet().isEmpty())
            throw new Exception("No DataFrame have been configured yet");
        if (header.size() > this.getContent().keySet().size())
            throw new Exception("Given header list exceed the current DataFrame header size (" + header.size() + " instead of " + this.getContent().keySet().size() + ")");
        if (header.stream().noneMatch(val -> this.getContent().containsKey(val)))
            throw new Exception("Not existing header value given");
    }

    private Integer retrieveIndexWhenAtDataFramePosition(Integer[] indexes, int i) {
        for (Integer index : indexes) {
            if (ArrayUtils.indexOf(this.getIndexes(), index) == i) return index;
        }
        return null;
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
        } else if (classParameterT.isAssignableFrom(Float.class)) {
            return (T) Float.valueOf(input);
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
        } else if (classParameterH.isAssignableFrom(Float.class)) {
            return (H) Float.valueOf(input);
        } else {
            throw new IllegalArgumentException("Bad type.");
        }
    }
}
