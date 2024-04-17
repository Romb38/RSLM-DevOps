import com.fasterxml.jackson.dataformat.csv.CsvReadException;
import org.example.DataFrame;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

import static org.junit.Assert.*;

public class DataFrameTest {

    private DataFrame<String, Double> dataFrame;

    @Before
    public void setUp() {
        // Initialiser le DataFrame avec quelques données
        HashMap<String, List<Double>> content = new HashMap<>();
        content.put("A", Arrays.asList(1.0, 2.0, 3.0, 4.0));
        content.put("B", Arrays.asList(2.0, 3.0, 4.0, 5.0));
        dataFrame = new DataFrame<>(content, String.class, Double.class);
    }

    @Test
    public void testSum() {
        assertEquals(10.0, dataFrame.sum("A"), 0.001);
        assertEquals(14.0, dataFrame.sum("B"), 0.001);
    }

    @Test
    public void testAverage() {
        assertEquals(2.5, dataFrame.average("A"), 0.001);
        assertEquals(3.5, dataFrame.average("B"), 0.001);
    }

    @Test
    public void testMin() {
        assertEquals(Double.valueOf(1.0), dataFrame.min("A"));
        assertEquals(Double.valueOf(2.0), dataFrame.min("B"));
    }

    @Test
    public void testMax() {
        assertEquals(Double.valueOf(4.0), dataFrame.max("A"));
        assertEquals(Double.valueOf(5.0), dataFrame.max("B"));
    }

    @Test
    public void testEmptyColumnStatistics() {
        // Ajouter une colonne vide pour tester le comportement avec des colonnes vides
        dataFrame.getContent().put("C", List.of());
        assertNull(dataFrame.min("C"));  // Min devrait retourner null
        assertNull(dataFrame.max("C"));  // Max devrait retourner null

        assertTrue(Double.isNaN(dataFrame.average("C")));
    }

    @Test(expected = Exception.class)
    public void testNonExistentColumn() {
        // Tester les calculs sur une colonne qui n'existe pas, devrait lancer une exception
        dataFrame.sum("Z");
    }

    @Test
    public void testSumWithNullAndNegativeValues() {
        dataFrame.getContent().put("C", Arrays.asList(-1.0, null, 2.0, -3.0));
        assertEquals(-2.0, dataFrame.sum("C"), 0.001);  // Vérifie que la somme est correcte malgré les valeurs null
    }


    @Test
    public void testAverageWithNullValues() {
        dataFrame.getContent().put("C", Arrays.asList(null, null, 2.0, null, 4.0));
        assertEquals(3.0, dataFrame.average("C"), 0.001);  // Teste la moyenne avec des valeurs null
    }

    @Test
    public void testSelectAdvanced() throws Exception {
        Map<String, Predicate<Double>> criteria = new HashMap<>();
        criteria.put("A", value -> value > 2.0);  // Sélectionner des valeurs > 2.0 dans la colonne 'A'

        DataFrame<String, Double> result = dataFrame.selectAdvanced(criteria);

        // Les résultats attendus après application des critères
        List<Double> expectedA = Arrays.asList(3.0, 4.0); // Seules les valeurs > 2.0 de 'A' restent
        List<Double> expectedB = Arrays.asList(4.0, 5.0); // Correspondant aux mêmes lignes dans 'B'

        // Vérification que les valeurs filtrées sont correctes pour toutes les colonnes affectées
        assertEquals(expectedA, result.getContent().get("A"));
        assertEquals(expectedB, result.getContent().get("B"));
    }

    @Test(expected = Exception.class)
    public void testSelectAdvancedWithInvalidHeader() throws Exception {
        // Passer un en-tête qui n'existe pas devrait lancer une exception
        Map<String, Predicate<Double>> criteria = new HashMap<>();
        criteria.put("Z", value -> value > 1.0);  // 'Z' n'existe pas
        dataFrame.selectAdvanced(criteria);
    }


    /**
     * @brief Test de création avec un path invalide
     */
    @Test(expected = FileNotFoundException.class)
    public void noCSVTestCreation() throws IOException {
        DataFrame<String, Integer> dataFrame = new DataFrame<>(Paths.get(""), ';', String.class, Integer.class);
    }

    /**
     * @brief Test de création avec un path valide
     */
    @Test
    public void basicCSVTestCreation() throws IOException {
        DataFrame<String, Integer> dataFrame = new DataFrame<>(Paths.get("src/test/java/exemple.csv").toAbsolutePath(), ';', String.class, Integer.class);
    }

    /**
     * @brief Test de création sans préciser le délimiteur
     */
    @Test
    public void basicCSVTestCreationNoDelimiter() throws IOException {
        DataFrame<String, Integer> dataFrame = new DataFrame<>(Paths.get("src/test/java/exemple.csv").toAbsolutePath(), String.class, Integer.class);
    }

    /**
     * @brief Test de création avec un fichier csv sans header
     */
    @Test
    public void basicCSVTestCreationNoHeader() throws Exception {
        DataFrame<String, Integer> dataFrame = new DataFrame<>(Paths.get("src/test/java/noheader.csv").toAbsolutePath(), null, String.class, Integer.class);
        System.out.println(dataFrame.toStringDisplay());
    }

    /**
     * @brief Test de création avec un csv vide
     */
    @Test(expected = CsvReadException.class)
    public void emptyCSVTestCreationNoDelimiter() throws IOException {
        DataFrame<String, Integer> dataFrame = new DataFrame<>(Paths.get("src/test/java/empty.csv").toAbsolutePath(), null, String.class, Integer.class);
    }

    @Test
    public void basicHeaderLineTestCreation() throws Exception {
        List<String> header = Arrays.asList("a", "b", "c");

        List<List<Integer>> rowList = new ArrayList<>();

        rowList.add(Arrays.asList(1, 2, 3));
        rowList.add(Arrays.asList(4, 5, 6));
        rowList.add(Arrays.asList(7, 8, 9));

        DataFrame<String, Integer> dataFrame = new DataFrame<>(header, rowList, String.class, Integer.class);
    }

    @Test
    public void testDataFrameCreation() {
        // Préparation des données pour le test
        HashMap<String, List<Integer>> content = new HashMap<>();
        content.put("index1", List.of(1, 2, 3));
        content.put("index2", List.of(4, 5, 6));
        Integer[] indexes = {1, 2, 3};

        // Appel de la méthode DataFrame
        DataFrame<String, Integer> df = new DataFrame<>(content, indexes, String.class, Integer.class);
    }

    @Test
    public void testDataFrameCreationNoHeader() {
        // Préparation des données pour le test
        HashMap<String, List<Integer>> content = new HashMap<>();
        content.put("index1", List.of(1, 2, 3));
        content.put("index2", List.of(4, 5, 6));

        // Appel de la méthode DataFrame
        DataFrame<String, Integer> df = new DataFrame<>(content, String.class, Integer.class);
    }

    @Test
    public void testDataFrameCreationFromData() {
        List<String> header = Arrays.asList("A", "B", "C");
        List<List<Integer>> rows = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6)
        );
        DataFrame<String, Integer> df = new DataFrame<>(header, rows, String.class, Integer.class);
        assertEquals(3, df.getContent().size());

    }

    @Test
    public void testToStringDisplay() throws Exception {
        List<String> header = Arrays.asList("A", "B", "C");
        List<List<Integer>> rows = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6)
        );
        DataFrame<String, Integer> df = new DataFrame<>(header, rows, String.class, Integer.class);
        String expected = "+---+---+---+" + System.lineSeparator() +
                "| A | B | C |" + System.lineSeparator() +
                "+---+---+---+" + System.lineSeparator() +
                "| 1 | 2 | 3 |" + System.lineSeparator() +
                "+---+---+---+" + System.lineSeparator() +
                "| 4 | 5 | 6 |" + System.lineSeparator() +
                "+---+---+---+" + System.lineSeparator();
        assertEquals(expected, df.toStringDisplay());
    }


    @Test
    public void testToStringPartialDisplay() throws Exception {
        List<String> header = Arrays.asList("A", "B", "C");
        List<List<Integer>> rows = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7, 8, 9),
                Arrays.asList(10, 11, 12)
        );
        DataFrame<String, Integer> df = new DataFrame<>(header, rows, String.class, Integer.class);
        String expectedFirst2Lines = "+---+---+---+" + System.lineSeparator() +
                "| A | B | C |" + System.lineSeparator() +
                "+---+---+---+" + System.lineSeparator() +
                "| 1 | 2 | 3 |" + System.lineSeparator() +
                "+---+---+---+" + System.lineSeparator() +
                "| 4 | 5 | 6 |" + System.lineSeparator() +
                "+---+---+---+" + System.lineSeparator();
        String expectedLast2Lines = "+----+----+----+" + System.lineSeparator() +
                "| A  | B  | C  |" + System.lineSeparator() +
                "+----+----+----+" + System.lineSeparator() +
                "| 7  | 8  | 9  |" + System.lineSeparator() +
                "+----+----+----+" + System.lineSeparator() +
                "| 10 | 11 | 12 |" + System.lineSeparator() +
                "+----+----+----+" + System.lineSeparator();
        assertEquals(expectedFirst2Lines, df.toStringPartialDisplay(2, true));
        assertEquals(expectedLast2Lines, df.toStringPartialDisplay(2, false));
    }

    @Test
    public void testToStringDisplayByIndex() throws Exception {
        List<String> header = Arrays.asList("A", "B", "C");
        List<List<Integer>> rows = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7, 8, 9),
                Arrays.asList(10, 11, 12)
        );
        Integer[] indexes = {0, 2}; // Définir des index
        DataFrame<String, Integer> df = new DataFrame<>(header, rows, String.class, Integer.class);
        df.setIndexes(indexes); // Configurer les index
        String expected = "+---+---+---+---+" + System.lineSeparator() +
                "|   | A | B | C |" + System.lineSeparator() +
                "+---+---+---+---+" + System.lineSeparator() +
                "| 0 | 1 | 2 | 3 |" + System.lineSeparator() +
                "+---+---+---+---+" + System.lineSeparator() +
                "| 2 | 4 | 5 | 6 |" + System.lineSeparator() +
                "+---+---+---+---+" + System.lineSeparator();
        assertEquals(expected, df.toStringDisplayByIndex(indexes));
    }


    @Test
    public void testAccessData() {
        List<String> header = Arrays.asList("A", "B", "C");
        List<List<Integer>> rows = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6)
        );
        DataFrame<String, Integer> df = new DataFrame<>(header, rows, String.class, Integer.class);
        assertEquals(Integer.valueOf(1), df.getContent().get("A").getFirst());
        assertEquals(Integer.valueOf(5), df.getContent().get("B").get(1));
    }

    @Test
    public void testSetIndexes() {
        List<String> header = Arrays.asList("A", "B", "C");
        List<List<Integer>> rows = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6)
        );
        DataFrame<String, Integer> df = new DataFrame<>(header, rows, String.class, Integer.class);
        Integer[] indexes = {10, 20};
        df.setIndexes(indexes);
        assertArrayEquals(indexes, df.getIndexes());
    }

    @Test
    public void testToStringDisplayByHeader() throws Exception {
        List<String> header = Arrays.asList("A", "B", "C");
        List<List<Integer>> rows = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7, 8, 9)
        );
        DataFrame<String, Integer> df = new DataFrame<>(header, rows, String.class, Integer.class);
        List<String> newHeader = Arrays.asList("B", "C");
        String expected = "+---+---+" + System.lineSeparator() +
                "| B | C |" + System.lineSeparator() +
                "+---+---+" + System.lineSeparator() +
                "| 2 | 3 |" + System.lineSeparator() +
                "+---+---+" + System.lineSeparator() +
                "| 5 | 6 |" + System.lineSeparator() +
                "+---+---+" + System.lineSeparator() +
                "| 8 | 9 |" + System.lineSeparator() +
                "+---+---+" + System.lineSeparator();
        assertEquals(expected, df.toStringDisplayByHeader(newHeader));
    }

    @Test(expected = Exception.class)
    public void testExceptionHandling() throws Exception {
        List<String> header = Arrays.asList("A", "B", "C");
        List<List<Integer>> rows = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5)
        ); // Une ligne a moins de valeurs que le nombre de colonnes
        new DataFrame<>(header, rows, String.class, Integer.class);
        fail("Expected an exception but none was thrown");
    }

    @Test
    public void testAccessDataByIndex() {
        List<String> header = Arrays.asList("A", "B", "C");
        List<List<Integer>> rows = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7, 8, 9)
        );
        DataFrame<String, Integer> df = new DataFrame<>(header, rows, String.class, Integer.class);
        assertEquals(Integer.valueOf(2), df.getContent().get("B").getFirst()); // Accès par index 0
        assertEquals(Integer.valueOf(9), df.getContent().get("C").get(2)); // Accès par index 2


    }

    @Test
    public void testNegativeLinesForPartialDisplay() throws Exception {
        List<String> header = Arrays.asList("A", "B", "C");
        List<List<Integer>> rows = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6)
        );
        DataFrame<String, Integer> df = new DataFrame<>(header, rows, String.class, Integer.class);
        String expected = "+---+---+---+" + System.lineSeparator() +
                "| A | B | C |" + System.lineSeparator() +
                "+---+---+---+" + System.lineSeparator();
        assertEquals(expected, df.toStringPartialDisplay(-1, true)); // Nombre négatif de lignes
    }
}