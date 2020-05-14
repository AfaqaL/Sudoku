import org.junit.jupiter.api.*;

import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MPModelTest {
    private static Connection con;
    @BeforeAll
    public static void run() throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(
                "jdbc:mysql://localhost",
                "Enter your username",
                "Enter your Password"
        );
        setUpDatabase();
    }

    private static void setUpDatabase() throws SQLException {
        Statement stm = con.createStatement();
        stm.execute("CREATE DATABASE metropolis_testdb;");
        stm.execute("USE metropolis_testdb;");
        stm.execute("CREATE TABLE metropolises (\n" +
                "metropolis CHAR(64),\ncontinent CHAR(64),\npopulation BIGINT\n);");
    }

    private static void destructDatabase() throws SQLException, ClassNotFoundException {
        Statement stm = con.createStatement();
        stm.execute("DROP TABLE metropolises;");
        stm = con.createStatement();
        stm.execute("DROP DATABASE metropolis_testdb");
        con.close();
    }

    @Test
    @Order(1)
    void testValidAdd() throws SQLException{
        MPModel model = new MPModel(con);
        String[] info = new String[]{
                "Tbilisi", "Asia", "1200000"
        };
        List<String> ls = model.addAction(info);
        assertEquals(3, ls.size());
        assertEquals("Tbilisi", ls.get(0));
        assertEquals("Asia",ls.get(1));
        assertEquals("1200000",ls.get(2));
    }

    @Test
    @Order(2)
    void testInvalidAdd() throws SQLException{
        MPModel model = new MPModel(con);
        String[] info = new String[]{
                "", "Europe", "127"
        };
        List<String> ls = model.addAction(info);
        assertEquals(null, ls);
    }


    @Test
    @Order(3)
    void testSearch() throws SQLException{
        MPModel model = new MPModel(con);
        String[] info = new String[]{
                "", "", ""
        };
        List<List<String>> data = model.searchAction(info, "Larger", "Exact");
        assertEquals(1, data.size());

        info[0] = "lisi";
        data = model.searchAction(info, "Larger", "Partial");
        assertEquals(1, data.size());

        info[1] = "As";

        data = model.searchAction(info, "Lower", "Partial");
        assertEquals(1, data.size());

        info[0] = "";
        info[2] = "10000";

        data = model.searchAction(info, "Larger", "Partial");
        assertEquals(1, data.size());

        data = model.searchAction(info, "Lower", "Exact");

        assertEquals(0, data.size());

    }

    @Test
    @Order(4)
    void testMultiAdd() throws SQLException{
        MPModel model = new MPModel(con);
        List<String> ls = model.addAction(new String[]{
                "New York", "North America", "1500"
        });

        assertEquals(3, ls.size());
        assertEquals("New York", ls.get(0));
        assertEquals("North America",ls.get(1));
        assertEquals("1500",ls.get(2));
    }

    @Test
    @Order(5)
    void testMultiSearch() throws  SQLException{
        MPModel model = new MPModel(con);
        String[] info = new String[]{
                "New York", "", ""
        };
        List<List<String>> data = model.searchAction(info, "Larger", "Exact");

        assertEquals(1, data.size());
        info[2] = "1400";

        data = model.searchAction(info, "Larger", "Partial");

        assertEquals(1, data.size());

        info[0] = "";
        info[2] = "1500000";

        data = model.searchAction(info, "Lower", "Exact");

        assertEquals(2, data.size());
        assertEquals(3, data.get(0).size());
        assertEquals(3, data.get(1).size());
    }

    @AfterAll
    public static void clear() throws SQLException, ClassNotFoundException {
        destructDatabase();
    }
}