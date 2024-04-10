import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

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
    public void testAddition() {

        assertEquals(4, 2 + 2);
    }

    @Test
    public void testSubtraction() {

        assertEquals(2, 4 - 2);
    }

    @Test
    public void testEquals(){
        //Test equals
        Object[] data1 = {1, 2, 3};
        Object[] data2 = {"a", "b", "c"};
        Object[][] tab1={data1,data2};
        Object[][] tab2={data2,data1};
        DataFrame df1 = new DataFrame(tab1);
        DataFrame df2 = new DataFrame(tab1);
        DataFrame df3 = new DataFrame(tab2);
        //Test sur les DataFrame complets
        assertTrue(df1.equals(df2));
        assertFalse(df1.equals(df3));
        //Test sur les colonnes
        assertTrue(df1.selectColumn(0).equals(df2.selectColumn(0)));
        assertFalse(df1.selectColumn(0).equals(df3.selectColumn(0)));
        //Test sur les lignes
        assertTrue(df1.selectRow(0).equals(df2.selectRow(0)));
        assertFalse(df1.selectRow(0).equals(df3.selectRow(0)));

    }

    @Test
    public void testGet(){
        //Test get
        Object[] data1 = {1, 2, 3};
        Object[] data2 = {"a", "b", "c"};
        Object[][] tab={data1,data2};
        DataFrame df = new DataFrame(tab);
        assertEquals(1, df.get(0, 0));
        assertEquals("a", df.get(0, 1));
        assertEquals({1, 2, 3},df.get(0));
    }

    @Test
    public void testDataFrameCreation() {
        // Test creation Dataframe depuis constructeurs vide
        DataFrame df = new DataFrame();
        assertNotNull(df);
    }

    @Test
    public void testDataFrameCreation() {
        // Test creation Dataframe depuis un tableau
        int[][] tab={{1, 2, 3},{4, 5, 6},{7, 8, 9}};
        DataFrame df = new DataFrame(tab);
        assertNotNull(df);
    }

    @Test
    public void testDataFrameCreationFromCSV() {
        // Test creation DataFrame depuis CSV
        String filePath = "example.csv";
        DataFrame df = DataFrame.fromCSV(filePath);
        assertNotNull(df);
    }

    @Test
    public void testPrintDataFrame() {
        // Test affichage du DataFrame complet
        Object[] data1 = {1, 2, 3};
        Object[] data2 = {"a", "b", "c"};
        Object[][] tab={data1,data2};
        DataFrame df = new DataFrame(tab);
        df.print();
    }

    @Test
    public void testPrintFirstRows() {
        // Test affichage première ligne
        Object[] data1 = {1, 2, 3};
        Object[] data2 = {"a", "b", "c"};
        Object[][] tab={data1,data2};
        DataFrame df = new DataFrame(tab);
        df.printFirstRows(2);
    }

    @Test
    public void testPrintLastRows() {
        // Test affichage dernière ligne
        Object[] data1 = {1, 2, 3};
        Object[] data2 = {"a", "b", "c"};
        Object[][] tab={data1,data2};
        DataFrame df = new DataFrame(tab);
        df.printLastRows(2);
    }

    @Test
    public void testSelectSubsetOfRows() {
        // Test selection sous-ensemble de lignes à partir de leur index
        Object[] data1 = {1, 2, 3};
        Object[] data2 = {"a", "b", "c"};
        Object[][] tab={data1,data2};
        DataFrame df = new DataFrame(tab);
        DataFrame subset = df.selectRows(new int[]{0, 2});
        assertNotNull(subset);
    }

    @Test
    public void testSelectSubsetOfColumns() {
        //Test selection sous-ensemble de colonnes à partir de leur index
        Object[] data1 = {1, 2, 3};
        Object[] data2 = {"a", "b", "c"};
        Object[][] tab={data1,data2};
        DataFrame df = new DataFrame(tab);
        DataFrame subset = df.selectColumns(new int[]{0,2});
        assertNotNull(subset);
    }

    @Test
    public void testMean(){
        // Test fonction de moyennage
        Object[] data1 = {1, 2, 3};
        Object[] data2 = {4, 5, 6};
        Object[][] tab={data1,data2};
        DataFrame df = new DataFrame(tab);
        Object[] means = df.means();
        assertNotNull(means);
        assertEquals(2.0, means.get(0));
        assertEquals(5.0, means.get(1));
    }

    @Test
    public void testMin(){
        // Test fonction de recherhce des min
        Object[] data1 = {1, 2, 3};
        Object[] data2 = {4, 5, 6};
        Object[][] tab={data1,data2};
        DataFrame df = new DataFrame(tab);
        Object[] min = df.min();
        assertNotNull(min);
        assertEquals(1, means.get(0));
        assertEquals(4, means.get(1));
    }

    @Test
    public void testMax(){
        // Test fonction de recherche des max
        Object[] data1 = {1, 2, 3};
        Object[] data2 = {4, 5, 6};
        Object[][] tab={data1,data2};
        DataFrame df = new DataFrame(tab);
        Object[] max = df.max();
        assertNotNull(max);
        assertEquals(3, means.get(0));
        assertEquals(6, means.get(1));
    }

    @Test
    public void testProcessFileInputValide(){
        //Test processFileInput(Scanner scanner)
        //ATTENTION un fichier CSV test.csv doit être disponible
        String input = "src/test/test.csv\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);

        assertTrue(result.containsKey("a"));
        assertTrue(result.containsKey("b"));
        assertEquals(3, result.get("a").length);
        assertEquals(4, result.get("b").length);
        assertEquals("1", result.get("a")[0].toString());
        assertEquals("9", result.get("b")[0].toString());
    }

    @Test
    public void testProcessFileInputInvalide(){
        //Test processFileInput(Scanner scanner)
        String input = "src/test/invalide_test.csv\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);
        assertThrows(IOException.class, () -> {
            Main.processFileInput(scanner);
        });
    }

    @Test
    public void testProcessManualInputValide(){
        //Test processManualInput(Scanner scanner, ObjectMapper objectMapper)
        Scanner scanner=new Scanner();
        ObjectMapper objectMapper = new ObjectMapper ();
        HashMap<String, Object[]> table = new HashMap<>();
        assertNotNull(table.processManualInput(scanner, objectMapper));

        //test entrée valide
        String input = "id\ny\n[\"value1\",\"value2\"]\n[\"value3\",\"value4\"]\nn";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        HashMap<String, Object[]> result = Main.processManualInput(scanner, objectMapper);
        assertTrue(result.containsKey(("id")));
        assertEquals(2, result.get("id").length);
        //TODO un test pour les entrée y/anything ???
        assertEquals("value1", result.get("id")[0]);
        assertEquals("value3", result.get("id")[1]);
 }

    @Test
    public void testProcessManualInputInvalide(){
        //Test processManualInput(Scanner scanner, ObjectMapper objectMapper)
        //test entrée colonne invalide
        String input = "id\ny\n[\"value1\"]\n[\"value2\", \"value3\"]\nn";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);
        ObjectMapper objectMapper = new ObjectMapper();

        assertThrows(JsonProcessingException.class, () -> {
            Main.processManualInput(scanner, objectMapper);
        });

        //test entrée ligne invalide
        String input = "id\ny\n[\"value2\", \"value3\"]\n[\"value1\"]\nn";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);
        ObjectMapper objectMapper = new ObjectMapper();

        assertThrows(JsonProcessingException.class, () -> {
            Main.processManualInput(scanner, objectMapper);
        });

    }


    @Test
    public void testInputMapToString(){
        //Test inputMapToString(HashMap<String, Object[]> inputArray)
        HashMap<String, Object[]> inputMap = new HashMap<>();
        inputMap.put("a", new Object[]{1, 2, 3});
        inputMap.put("b", new Object[]{4, 5, 6});

        String expected :"------------------------\n"+
                        "|a|b[\n"+"|1|4|\n"+"|2|5|\n"+"|3|6|\n"+
                        "------------------------\n";
        assertEquals(expected, Main.inputMapToString(insputMap));
    }

    @Test
    public void test buildTableDelimiter(){
        StringBuilder sb= new StringBuilder();
        int size = 1;
        assertEquals("--------",Main.buildTableDelimiter(sb,size));
    }
}
