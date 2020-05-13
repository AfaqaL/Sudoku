import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost",
                    "root",
                    "Afaqinio_123"
            );
            Statement stm = con.createStatement();
            stm.execute("USE metropolisschema");
            MPModel model = new MPModel(con);
            MPView view = new MPView();
            MPController controller = new MPController(model, view);
            controller.start();

        } catch (ClassNotFoundException classExc) {
            System.out.println("class not found");;
        } catch (SQLException sqlExc) {
            sqlExc.printStackTrace();
        }
    }

}
