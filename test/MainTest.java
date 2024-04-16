import org.junit.Test;
import static org.junit.Assert.*;

public class MainTest {
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


}
