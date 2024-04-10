package org.example;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.HashMap;

public class DataFrameUtils {

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
     * @param dataFrame Dataframe to display
     * @return Result to display
     */
    public static String inputMapToString(DataFrame dataFrame) {
        HashMap<String, Object[]> inputArray = dataFrame.getContent();
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
}
