import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnitTest {

    /**
     * @brief Test d'égalité
     *
     * @details Cette méthode permet de tester si 1 est égal à 1
     */
 @Test
    public void testEquality() {
        // Assertion pour vérifier si 1 est égal à 1
        assertEquals(1, 1);
    }

    @Test
    public void testInequality() {
        // Assertion pour vérifier si 1 est égal à 1
        assertEquals(2, 1);
    }
}
