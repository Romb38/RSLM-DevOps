import org.example.DataFrame;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class autoTesting {

    @Test
    public void testDataFrameCreationFromCSV() throws IOException {
        Path path = Paths.get("src/test/java/exemple.csv").toAbsolutePath(); // Modifier le chemin vers le fichier CSV
        DataFrame<String, String> df = new DataFrame<>(path, String.class, String.class);
        assertEquals(3, df.getContent().size());
        // Ajoutez d'autres assertions pour vérifier les valeurs, les types, etc.
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
        // Ajoutez d'autres assertions pour vérifier les valeurs, les types, etc.
    }

    @Test
    public void testToStringDisplay() throws Exception {
        List<String> header = Arrays.asList("A", "B", "C");
        List<List<Integer>> rows = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6)
        );
        DataFrame<String, Integer> df = new DataFrame<>(header, rows, String.class, Integer.class);
        String expected = "---------------------\n" +
                "|     A     |   B   |   C   |\n" +
                "---------------------\n" +
                "|     1     |   2   |   3   |\n" +
                "|     4     |   5   |   6   |\n" +
                "---------------------\n";
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
        String expectedFirst2Lines = "---------------------\n" +
                "|     A     |   B   |   C   |\n" +
                "---------------------\n" +
                "|     1     |   2   |   3   |\n" +
                "|     4     |   5   |   6   |\n" +
                "---------------------\n";
        String expectedLast2Lines = "---------------------\n" +
                "|     A     |   B   |   C   |\n" +
                "---------------------\n" +
                "|     7     |   8   |   9   |\n" +
                "|    10     |  11   |   12  |\n" +
                "---------------------\n";
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
        String expected = "---------------------\n" +
                "|     A     |   B   |   C   |\n" +
                "---------------------\n" +
                "|     1     |   2   |   3   |\n" +
                "|     7     |   8   |   9   |\n" +
                "---------------------\n";
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
        assertEquals(Integer.valueOf(1), df.getContent().get("A").get(0));
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
        String expected = "---------------------\n" +
                "|   B   |   C   |\n" +
                "---------------------\n" +
                "|   2   |   3   |\n" +
                "|   5   |   6   |\n" +
                "|   8   |   9   |\n" +
                "---------------------\n";
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
        assertEquals(Integer.valueOf(2), df.getContent().get("B").get(0)); // Accès par index 0
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
        String expected = "---------------------\n" +
                "|     A     |   B   |   C   |\n" +
                "---------------------\n";
        assertEquals(expected, df.toStringPartialDisplay(-1, true)); // Nombre négatif de lignes
    }
}
