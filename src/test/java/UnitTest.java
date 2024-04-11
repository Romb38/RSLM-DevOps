import com.fasterxml.jackson.dataformat.csv.CsvReadException;
import org.example.DataFrame;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class UnitTest {

    /**
     * @brief Test d'égalité
     * @details Cette méthode permet de tester si 1 est égal à 1
     */
    @Test
    public void testEquality() {
        // Assertion pour vérifier si 1 est égal à 1
        assertEquals(1, 1);
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

        DataFrame<String, Integer> dataFrame = new DataFrame<>(header,rowList, String.class, Integer.class);
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
}
