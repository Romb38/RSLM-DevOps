package org.example;


import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DataFrame<H, T extends Number & Comparable<T>> {
    private final Class<T> classParameterT;
    private final Class<H> classParameterH;
    private Integer[] indexes;
    private HashMap<H, List<T>> content = new HashMap<>();


    /**
     * @param path file path
     * @throws IOException If unable to open the file
     * @brief Read CSV file for a given file path
     * @details Note: csv default delimiter character is `;`
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
     * @param path file path
     * @throws IOException If unable to open the file
     * @brief Read CSV file for a given file path
     * @details Note: csv default delimiter character is `;`
     */
    public DataFrame(Path path, Class<H> classParameterH, Class<T> classParameterT) throws IOException {
        this(path, ';', classParameterH, classParameterT);
    }

    /**
     * @param header list of header to declare
     * @param rows   list of rows to declare
     * @brief Build a DataFrame from header and rows
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
     * @param content Data frame content shaped has a map
     * @param indexes Data frame indexes
     * @brief Build a DataFrame from content Map and indexes
     */
    public DataFrame(HashMap<H, List<T>> content, Integer[] indexes, Class<H> classParameterH, Class<T> classParameterT) {
        this.classParameterT = classParameterT;
        this.classParameterH = classParameterH;
        this.setContent(content);
        this.setIndexes(indexes);
    }

    /**
     * @param content Data frame content shaped has a map
     * @brief Build a DataFrame from content Map
     */
    public DataFrame(HashMap<H, List<T>> content, Class<H> classParameterH, Class<T> classParameterT) {
        this.classParameterT = classParameterT;
        this.classParameterH = classParameterH;
        this.setContent(content);
    }

    /**
     * @return indexes array
     * @brief Getter for the DataFrame index array
     */
    public Integer[] getIndexes() {
        return indexes;
    }

    /**
     * @param indexes array
     * @brief Setter for the DataFrame index array
     */
    public void setIndexes(Integer[] indexes) {
        this.indexes = indexes;
    }

    /**
     * @return map of element by column header
     * @brief Getter for the DataFrame content map
     */
    public HashMap<H, List<T>> getContent() {
        return content;
    }

    /**
     * @param content map of element by column header
     * @brief Setter for the DataFrame Content map
     */
    public void setContent(HashMap<H, List<T>> content) {
        this.content = content;
    }

    /**
     * @param indexes to filter on
     * @return selected DataFrame
     * @throws Exception Bad index selection
     * @brief Select a part of the current DataFrame by using indexes and return a new DataFrame
     */
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

    /**
     * @param columns to filter on
     * @return selected DataFrame
     * @throws Exception Bad column selection
     * @brief Select a part of the current DataFrame by using columns and return a new DataFrame
     */
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
     * @return String representing the DataFrame in a table format.
     * @brief Display the DataFrame in a well-aligned table format, including predefined indices if available.
     * @details <pre>Example of result:
     * +---+----+----+
     * |   | a  | c  |
     * +---+----+----+
     * | 1 | 10 | 30 |
     * +---+----+----+
     * | 2 | 15 | 35 |
     * +---+----+----+</pre>
     */
    public String toStringDisplay() {
        // Calculate the maximum width for each column based on header and data
        Map<H, Integer> columnWidths = new HashMap<>();
        for (H header : this.getContent().keySet()) {
            int maxWidth = header.toString().length(); // Start with the header's length
            for (T value : this.getContent().get(header)) {
                maxWidth = Math.max(maxWidth, value != null ? value.toString().length() : 0);
            }
            columnWidths.put(header, maxWidth);
        }

        StringBuilder builder = new StringBuilder();
        boolean hasIndex = hasIndex();
        int indexWidth = hasIndex ? Arrays.stream(this.getIndexes()).map(Object::toString).max(Comparator.comparingInt(String::length)).orElse("").length() : 0;

        // Build the table delimiter with or without index
        buildTableDelimiter(builder, columnWidths, indexWidth, hasIndex);

        // Header row
        builder.append("|");
        if (hasIndex) {
            builder.append(String.format(" %-" + indexWidth + "s |", ""));
        }
        for (H header : this.getContent().keySet()) {
            builder.append(String.format(" %-" + columnWidths.get(header) + "s |", header));
        }
        builder.append(System.lineSeparator());
        buildTableDelimiter(builder, columnWidths, indexWidth, hasIndex);

        // Data rows
        int numberOfRows = this.getContent().values().iterator().next().size();
        for (int i = 0; i < numberOfRows; i++) {
            builder.append("|");
            if (hasIndex) {
                builder.append(String.format(" %-" + indexWidth + "s |", this.getIndexes()[i]));
            }
            for (H header : this.getContent().keySet()) {
                List<T> column = this.getContent().get(header);
                String value = column.size() > i && column.get(i) != null ? column.get(i).toString() : "";
                builder.append(String.format(" %-" + columnWidths.get(header) + "s |", value));
            }
            builder.append(System.lineSeparator());
            buildTableDelimiter(builder, columnWidths, indexWidth, hasIndex);
        }

        return builder.toString();
    }

    /**
     * @param builder      StringBuilder to which the delimiter line is appended.
     * @param columnWidths Map with the widths for each column.
     * @param indexWidth   Width of the index column, if indices are used.
     * @param hasIndex     Boolean indicating if indices are present.
     * @brief Builds the delimiter line for the table based on column widths, including index if exists.
     */
    private void buildTableDelimiter(StringBuilder builder, Map<H, Integer> columnWidths, int indexWidth, boolean hasIndex) {
        builder.append("+");
        if (hasIndex) {
            builder.append("-".repeat(indexWidth + 2)).append("+"); // +2 for the padding on both sides
        }
        for (Integer width : columnWidths.values()) {
            builder.append("-".repeat(width + 2)).append("+"); // +2 for the padding on both sides
        }
        builder.append(System.lineSeparator());
    }

    /**
     * @return String result to display
     * @brief Display in a table the given dataframe for the given indexes
     * @details <pre>Example of result:
     * +---+----+----+
     * |   | a  | c  |
     * +---+----+----+
     * | 1 | 10 | 30 |
     * +---+----+----+
     * | 2 | 15 | 35 |
     * +---+----+----+</pre>
     */
    public String toStringDisplayByIndex(Integer[] indexes) throws Exception {
        checkIndexInput(indexes);
        // Calculate the maximum width for each column based on header and data
        Map<H, Integer> columnWidths = new HashMap<>();
        int numberOfRows = this.getContent().values().iterator().next().size();
        Integer index;
        for (H header : this.getContent().keySet()) {
            int maxWidth = header.toString().length(); // Start with the header's length
            List<T> column = this.getContent().get(header);
            for (int i = 0; i < numberOfRows; i++) {
                index = retrieveIndexWhenAtDataFramePosition(indexes, i);
                if (index != null) {
                    maxWidth = Math.max(maxWidth, column.get(i) != null ? column.get(i).toString().length() : 0);
                }
            }
            columnWidths.put(header, maxWidth);
        }

        StringBuilder builder = new StringBuilder();
        int indexWidth = getIndexWidth();
        buildTableDelimiter(builder, columnWidths, indexWidth, true);

        builder.append("|").append(String.format(" %-" + indexWidth + "s |", ""));
        for (H header : this.getContent().keySet()) {
            builder.append(String.format(" %-" + columnWidths.get(header) + "s |", header));
        }

        builder.append(System.lineSeparator());
        // Build the table delimiter with or without index
        buildTableDelimiter(builder, columnWidths, indexWidth, true);

        for (int i = 0; i < numberOfRows; i++) {
            index = retrieveIndexWhenAtDataFramePosition(indexes, i);
            if (index != null) {
                builder.append("|").append(String.format(" %-" + indexWidth + "s |", this.getIndexes()[i]));
                for (H header : this.getContent().keySet()) {
                    List<T> column = this.getContent().get(header);
                    String value = column.size() > i && column.get(i) != null ? column.get(i).toString() : "";
                    builder.append(String.format(" %-" + columnWidths.get(header) + "s |", value));
                }
                builder.append(System.lineSeparator());
                buildTableDelimiter(builder, columnWidths, indexWidth, true);
            }
        }
        return builder.toString();
    }

    /**
     * @return max index string length
     * @brief Retrieve max the String length of the current index
     */
    private int getIndexWidth() {
        return Arrays.stream(this.getIndexes()).map(Object::toString).max(Comparator.comparingInt(String::length)).orElse("").length();
    }

    /**
     * @return String result to display
     * @brief Display in a table the given dataframe for the given indexes
     * @details <pre>Example of result:
     * +---+----+----+
     * |   | a  | c  |
     * +---+----+----+
     * | 1 | 10 | 30 |
     * +---+----+----+
     * | 2 | 15 | 35 |
     * +---+----+----+</pre>
     */
    public String toStringDisplayByHeader(List<H> columnFilter) throws Exception {
        checkHeaderInput(columnFilter);
        // Calculate the maximum width for each column based on header and data
        Map<H, Integer> columnWidths = new HashMap<>();
        for (H header : columnFilter) {
            int maxWidth = header.toString().length(); // Start with the header's length
            for (T value : this.getContent().get(header)) {
                maxWidth = Math.max(maxWidth, value != null ? value.toString().length() : 0);
            }
            columnWidths.put(header, maxWidth);
        }

        StringBuilder builder = new StringBuilder();
        boolean hasIndex = hasIndex();
        int indexWidth = hasIndex ? getIndexWidth() : 0;

        // Build the table delimiter with or without index
        buildTableDelimiter(builder, columnWidths, indexWidth, hasIndex);

        // Header row
        builder.append("|");
        if (hasIndex) {
            builder.append(String.format(" %-" + indexWidth + "s |", ""));
        }
        for (H header : columnFilter) {
            builder.append(String.format(" %-" + columnWidths.get(header) + "s |", header));
        }
        builder.append(System.lineSeparator());
        buildTableDelimiter(builder, columnWidths, indexWidth, hasIndex);

        // Data rows
        int numberOfRows = this.getContent().values().iterator().next().size();
        for (int i = 0; i < numberOfRows; i++) {
            builder.append("|");
            if (hasIndex) {
                builder.append(String.format(" %-" + indexWidth + "s |", this.getIndexes()[i]));
            }
            for (H header : columnFilter) {
                List<T> column = this.getContent().get(header);
                String value = column.size() > i && column.get(i) != null ? column.get(i).toString() : "";
                builder.append(String.format(" %-" + columnWidths.get(header) + "s |", value));
            }
            builder.append(System.lineSeparator());
            buildTableDelimiter(builder, columnWidths, indexWidth, hasIndex);
        }

        return builder.toString();
    }

    /**
     * @param nbLines number of lines to display
     * @param mode    true = first n lines // false = last n lines
     * @return String result to display
     * @brief Display in a table the given dataframe
     * @details <pre>Example of result:
     * +---+----+----+
     * |   | a  | c  |
     * +---+----+----+
     * | 1 | 10 | 30 |
     * +---+----+----+
     * | 2 | 15 | 35 |
     * +---+----+----+</pre>
     */
    public String toStringPartialDisplay(int nbLines, boolean mode) {
        if (nbLines < 0)
            nbLines = 0;
        HashMap<H, List<T>> inputArray = this.getContent();
        int maxElement = mode ? nbLines : inputArray.values().stream().max(Comparator.comparing(List::size)).map(List::size).orElse(0);
        int initialValue = mode ? 0 : Math.max(0, inputArray.values().stream().max(Comparator.comparing(List::size)).map(List::size).orElse(0) - nbLines);
        // Calculate the maximum width for each column based on header and data
        Map<H, Integer> columnWidths = new HashMap<>();
        for (H header : this.getContent().keySet()) {
            int maxWidth = header.toString().length(); // Start with the header's length
            List<T> column = this.getContent().get(header);
            for (int i = initialValue; i < maxElement; i++) {
                maxWidth = Math.max(maxWidth, column.get(i) != null ? column.get(i).toString().length() : 0);
            }
            columnWidths.put(header, maxWidth);
        }

        StringBuilder builder = new StringBuilder();
        boolean hasIndex = hasIndex();
        int indexWidth = hasIndex ? getIndexWidth() : 0;

        // Build the table delimiter with or without index
        buildTableDelimiter(builder, columnWidths, indexWidth, hasIndex);

        // Header row
        builder.append("|");
        if (hasIndex) {
            builder.append(String.format(" %-" + indexWidth + "s |", ""));
        }
        for (H header : this.getContent().keySet()) {
            builder.append(String.format(" %-" + columnWidths.get(header) + "s |", header));
        }
        builder.append(System.lineSeparator());
        buildTableDelimiter(builder, columnWidths, indexWidth, hasIndex);
        for (int i = initialValue; i < maxElement; i++) {
            builder.append("|");
            if (hasIndex) {
                builder.append(String.format(" %-" + indexWidth + "s |", this.getIndexes()[i]));
            }
            for (H header : this.getContent().keySet()) {
                List<T> column = this.getContent().get(header);
                String value = column.size() > i && column.get(i) != null ? column.get(i).toString() : "";
                builder.append(String.format(" %-" + columnWidths.get(header) + "s |", value));
            }
            builder.append(System.lineSeparator());
            buildTableDelimiter(builder, columnWidths, indexWidth, hasIndex);
        }
        return builder.toString();
    }

    /**
     * @return true if the index array isn't null nor empty
     * @brief Check if the current DataFrame index are initialised
     */
    public boolean hasIndex() {
        return this.getIndexes() != null && this.getIndexes().length > 0;
    }

    /**
     * @param indexes array to check
     * @throws Exception check failed with reason
     * @brief Run a checklist to verify the array of index compare to the saved index array
     */
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

    /**
     * @param header list to check
     * @throws Exception check failed with reason
     * @brief Run a checklist to verify the list of header compare to the saved header
     */
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

    /**
     * @param indexes list of index
     * @param i       current position
     * @return found index at position, null otherwise
     * @brief Get index at referenced position
     */
    private Integer retrieveIndexWhenAtDataFramePosition(Integer[] indexes, int i) {
        for (Integer index : indexes) {
            if (ArrayUtils.indexOf(this.getIndexes(), index) == i) return index;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private T convertValueT(String input) {
        try {
            if (classParameterT.isAssignableFrom(Integer.class)) {
                return (T) Integer.valueOf(input);
            } else if (classParameterT.isAssignableFrom(Double.class)) {
                return (T) Double.valueOf(input);
            } else if (classParameterT.isAssignableFrom(Float.class)) {
                return (T) Float.valueOf(input);
            } else if (classParameterT.isAssignableFrom(Long.class)) {
                return (T) Long.valueOf(input);
            } else {
                throw new IllegalArgumentException("Unsupported type.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Input string is not a valid number: " + input);
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

    /**
     * @param header selected column
     * @brief Calculate sum of a column
     */
    public double sum(H header) {
        return this.getContent().get(header).stream()
                .filter(Objects::nonNull)  // Filtre pour ignorer les valeurs null
                .mapToDouble(Number::doubleValue)
                .sum();
    }


    /**
     * @param header selected column
     * @brief Calculate average of a column
     */
    public double average(H header) {
        return this.getContent().get(header).stream()
                .filter(Objects::nonNull)  // Filtre pour ignorer les valeurs null
                .mapToDouble(Number::doubleValue)
                .average()
                .orElse(Double.NaN);  // Retourne NaN si aucune valeur n'est présente après le filtrage
    }


    /**
     * @param header selected column
     * @brief Find minimum value in a column
     */
    public T min(H header) {
        return this.getContent().get(header).stream()
                .min(Comparator.naturalOrder())
                .orElse(null);  // Retourne null si la colonne est vide
    }

    /**
     * @param header selected column
     * @brief Find maximum value in a column
     */
    public T max(H header) {
        return this.getContent().get(header).stream()
                .max(Comparator.naturalOrder())
                .orElse(null);  // Retourne null si la colonne est vide
    }

    /**
     * @param criteria Map of headers to their respective predicates for selection.
     * @return A new DataFrame with only the rows that match all the criteria for any mentioned column.
     * @throws Exception If there are issues with the headers.
     * @brief Advanced selection based on custom predicates per column that filters entire rows.
     */
    public DataFrame<H, T> selectAdvanced(Map<H, Predicate<T>> criteria) throws Exception {
        if (criteria == null || criteria.isEmpty()) {
            throw new IllegalArgumentException("Criteria map cannot be null or empty.");
        }

        // Check for invalid headers
        for (H header : criteria.keySet()) {
            if (!this.getContent().containsKey(header)) {
                throw new Exception("Header not found in DataFrame: " + header);
            }
        }

        // Create a list of all headers for index reference
        List<H> allHeaders = new ArrayList<>(this.getContent().keySet());

        // Prepare to collect indexes of rows that match all criteria
        Set<Integer> validRowIndexes = new HashSet<>();
        for (int i = 0; i < this.getContent().get(allHeaders.getFirst()).size(); i++) {
            final int rowIndex = i;
            boolean rowMatches = allHeaders.stream()
                    .allMatch(header -> criteria.getOrDefault(header, val -> true).test(this.getContent().get(header).get(rowIndex)));
            if (rowMatches) {
                validRowIndexes.add(rowIndex);
            }
        }

        // Build new content map with only the valid rows
        HashMap<H, List<T>> filteredContent = new HashMap<>();
        for (H header : allHeaders) {
            List<T> originalList = this.getContent().get(header);
            List<T> filteredList = validRowIndexes.stream()
                    .map(originalList::get)
                    .collect(Collectors.toList());
            filteredContent.put(header, filteredList);
        }

        // Adjust indexes if they are used in the original DataFrame
        if (hasIndex()) {
            Integer[] newIndexes = validRowIndexes.stream().map(i -> this.getIndexes()[i]).toArray(Integer[]::new);
            return new DataFrame<>(filteredContent, newIndexes, classParameterH, classParameterT);
        } else {
            return new DataFrame<>(filteredContent, classParameterH, classParameterT);
        }
    }
}