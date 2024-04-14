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
        if (args.length > 0) {
            String csvFilePath = args[0];  // Utilisation des arguments de la ligne de commande pour le chemin du fichier
            DataFrame<String, Integer> dataFrame = new DataFrame<>(Paths.get(csvFilePath), ';', String.class, Integer.class);

            if (dataFrame.getContent().containsKey("a")) {
                System.out.println("\n Statistics for column 'a':");
                System.out.println("Sum: " + dataFrame.sum("a"));
                System.out.println("Average: " + dataFrame.average("a"));
                System.out.println("Min: " + dataFrame.min("a"));
                System.out.println("Max: " + dataFrame.max("a"));
            }

            System.out.println("\n Result: ");
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
            Integer[] indexes = {1, 2, 3, 4, 5};
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
}

