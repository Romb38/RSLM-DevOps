package org.example;

import java.nio.file.Paths;
import java.util.Arrays;

public class Main {

    /**
     * @brief Méthode principale
     * @details This method is the entry point of the program
     */
    public static void main(String[] args) throws Exception {
        // WORK IN PROGRESS
        String csvFilePath = System.getenv("CSV_FILE_PATH");
        DataFrame<String, Integer> dataFrame = new DataFrame<>(Paths.get(csvFilePath), ';', String.class, Integer.class);
        System.out.println("Result: ");
        System.out.println(dataFrame.toStringDisplay());
        System.out.println("Result - last 2: ");
        System.out.println(dataFrame.toStringPartialDisplay(2, false));
        System.out.println("Result - first 2: ");
        System.out.println(dataFrame.toStringPartialDisplay(2, true));
        System.out.println("Result - column a & c: ");
        System.out.println(dataFrame.toStringDisplayByHeader(Arrays.asList("a", "c")));
        DataFrame<String, Integer> dataFrameToc = dataFrame.selectDataFrameByColumn(Arrays.asList("b", "d"));
        System.out.println("Result - selection by column b & d: ");
        System.out.println(dataFrameToc.toStringDisplay());
        Integer[] indexes = {1, 2, 3, 4};
        dataFrame.setIndexes(indexes);
        System.out.println("Result with indexes setup: ");
        System.out.println(dataFrame.toStringDisplay());
        System.out.println("Result with indexes setup - last 2: ");
        System.out.println(dataFrame.toStringPartialDisplay(2, false));
        System.out.println("Result with indexes setup - first 2: ");
        System.out.println(dataFrame.toStringPartialDisplay(2, true));
        System.out.println("Result with indexes setup - first 2: ");
        System.out.println(dataFrame.toStringPartialDisplay(2, true));
        System.out.println("Result with indexes setup - column a & c: ");
        System.out.println(dataFrame.toStringDisplayByHeader(Arrays.asList("a", "c")));
        Integer[] indexesFilter = {1, 3};
        System.out.println("Result - with index 1 & 3: ");
        System.out.println(dataFrame.toStringDisplayByIndex(indexesFilter));
        DataFrame<String, Integer> dataFrameTic = dataFrame.selectDataFrameByIndex(indexesFilter);
        System.out.println("Result - selection by index 1 & 3: ");
        System.out.println(dataFrameTic.toStringDisplay());
        DataFrame<String, Integer> dataFrameTac = dataFrame.selectDataFrameByColumn(Arrays.asList("a", "c"));
        System.out.println("Result - selection by column a & c: ");
        System.out.println(dataFrameTac.toStringDisplay());
    }
}