import javax.management.Query;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MPModel {

    private final Connection con;
    private JTable table;

    String qr[] = new String[]{
            " SELECT (*) FROM metropolises ",
            " population > ",
            " population < ",
            " continent = ",
            " continent LIKE ",
            " metropolis = ",
            " metropolis LIKE",
            "\"",
            "%",
            " AND ",
            " WHERE "
    };


    public MPModel(Connection con){
        this.con = con;
    }

    public List<String> addAction(String[] info){

        try {
            Statement stm = con.createStatement();
            stm.execute("INSERT INTO metropolises VALUES (\"" + info[0] +
                    "\", \"" + info[1] +
                    "\"," + info[2] + ");");
            ArrayList<String> arr = new ArrayList<>(Arrays.asList(info));
            return arr;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }

    }

    private String buildQuery(StringBuilder query, boolean[] arrWhere, String[] info, String popul, String exact){
        query.append(" WHERE ");
        if(arrWhere[0]) {
            query.append("metropolis" + (exact.contains("Exact") ? " = '" + info[0] + "'"
                    : " LIKE '%" + info[0] + "%'"));
        }

        if(arrWhere[1]) {
            if (arrWhere[0])
                query.append(" AND ");

            query.append("continent" + (exact.contains("Exact") ? " = '" + info[1] + "'"
                    : " LIKE '%" + info[1] + "%'"));
        }
        if(arrWhere[2]) {
            if (arrWhere[1] || arrWhere[0])
                query.append(" AND ");

            query.append("population" + (popul.contains("Larger") ? " > " : " < ") + info[2]);
        }

        return query.toString();
    }
    public List<List<String>> searchAction(String[] info, String popul, String exact){

        StringBuilder query = new StringBuilder();
        boolean[] arrWhere = new boolean[]{false, false, false};
        boolean hasWhere = false;
        for(int i=0; i<info.length; i++) {
            if (!info[i].equals("")) {
                arrWhere[i] = true;
                hasWhere = true;
            }
        }
        String finalQuery = "SELECT * FROM metropolises";
        if(hasWhere) { finalQuery += buildQuery(query, arrWhere, info, popul, exact); }
        finalQuery += ";";

        System.out.println(finalQuery);
        return  getQueryRes(finalQuery);

    }
    private List<List<String>> getQueryRes(String query){
        System.out.println(query);
        try {
            Statement stm = con.createStatement();
            ResultSet queryData = stm.executeQuery(query);
            List<List<String>> ls = new ArrayList<>();
            while (queryData.next()) {
                List<String>  row = new ArrayList<>(3);
                row.add(queryData.getString(1));
                row.add(queryData.getString(2));
                row.add(queryData.getString(3));
                ls.add(row);
            }
            return ls;
        } catch (SQLException throwables) {
            System.out.println("Couldn't establish statement connection");
            return null;
        }
    }

    private static final int select = 0;
    private static final int popLarger = 1;
    private static final int popSmaller = 2;
    private static final int contSame = 3;
    private static final int contLike = 4;
    private static final int metSame = 5;
    private static final int metLike = 6;
    private static final int quote = 7;
    private static final int like = 8;
    private static final int and = 9;
    private static final int where = 10;
}
