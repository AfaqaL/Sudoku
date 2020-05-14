import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MPModel {

    private final Connection con;

    public MPModel(Connection con){
        this.con = con;
    }

    public List<String> addAction(String[] info) throws SQLException{
        Statement stm = con.createStatement();
        for (int i = 0; i < 3; i++) {
            if(info[i].equals("")) return null;
        }
        stm.execute("INSERT INTO metropolises VALUES (\"" + info[0] +
                "\", \"" + info[1] +
                "\"," + info[2] + ");");
        ArrayList<String> arr = new ArrayList<>(Arrays.asList(info));
        return arr;
    }

    public List<List<String>> searchAction(String[] info, String popul, String exact) throws SQLException{
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

        return  getQueryRes(finalQuery);

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
    private List<List<String>> getQueryRes(String query) throws SQLException{
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
    }
}
