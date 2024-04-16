package org.example;



import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.io.IOException;


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

            try {
                // Création d'un DataFrame d'exemple avec des données d'employés
                List<String> headers = Arrays.asList("Age", "Department", "Salary");
                List<List<Integer>> rows = Arrays.asList(
                        Arrays.asList(30, 1, 5000),  // Supposons que 'HR' = 1, 'Engineering' = 2, 'Marketing' = 3
                        Arrays.asList(45, 2, 7000),
                        Arrays.asList(29, 1, 4500),
                        Arrays.asList(35, 2, 5200),
                        Arrays.asList(42, 3, 6200)
                );


                // Création du DataFrame
                DataFrame<String, Integer> df = new DataFrame<>(headers, rows, String.class, Integer.class);
                System.out.println("Nouveau Dataframe : ");
                System.out.println(df.toStringDisplay());
                // Définir les critères de sélection avancée
                Map<String, Predicate<Integer>> criteria = new HashMap<>();
                criteria.put("Age", age -> age > 30); // Âge doit être supérieur à 30 ans
                criteria.put("Salary", salary -> salary >= 6000); // Salaire doit être supérieur ou égal à 6000

                // Application de la sélection avancée
                DataFrame<String, Integer> filteredDf = df.selectAdvanced(criteria);

                // Affichage du résultat
                System.out.println("result apres selection selon critères age > 30 et salaire > 6000 :");
                System.out.println(filteredDf.toStringDisplay());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        }

    }


