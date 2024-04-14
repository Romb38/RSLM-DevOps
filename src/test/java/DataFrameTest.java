import org.example.DataFrame;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
        dataFrame.getContent().put("C", Arrays.asList());
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


}
