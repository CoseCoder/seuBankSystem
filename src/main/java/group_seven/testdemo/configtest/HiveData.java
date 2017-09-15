package group_seven.testdemo.configtest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class HiveData {
	
	public  static String driverName = "org.apache.hive.jdbc.HiveDriver";

	public static void hiveLoadData(String tableName) {
		Connection con=null;
		try {
			Class.forName(driverName);
			con = DriverManager.getConnection("jdbc:hive2://localhost:10000/bank", "root", "595717");
			Statement stmt = con.createStatement();

			String truncate = "truncate table "+tableName;
			String loaddata = "load data inpath '/opt/"+tableName+"/output/part-r-00000' into table "+tableName;
			//清除旧数据
			System.out.println("清除旧数据");
			stmt.execute(truncate);
			//加载新数据
			System.out.println("加载新数据");
			stmt.execute(loaddata);

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}


}
