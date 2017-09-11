package group_seven.testdemo.configtest;

import java.sql.*;

/**
 * Created by mira on 8/23/17.
 */
public class HiveApi {
    public static String driverName = "org.apache.hive.jdbc.HiveDriver";

    public static void main(String[] a) {
        Connection con = null;
        try {
            Class.forName(driverName);
            con = DriverManager.getConnection("jdbc:hive2://localhost:10000/bank", "root", "123456");

            Statement stmt = con.createStatement();
            String data = "select * from car_score";

            stmt.execute(data);

            ResultSet rs = stmt.executeQuery(data);

            int columns = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    System.out.print(rs.getString(i));
                    System.out.print("\t\t");
                }
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
