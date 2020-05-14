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
                    "Enter your user",
                    "Enter your password"
            );
            Statement stm = con.createStatement();
            stm.execute("USE Your Database");
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
