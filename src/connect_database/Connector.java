package connect_database;

import java.sql.*;

public class Connector {
	// Change the parameters accordingly.
	public static String driverName = "com.mysql.jdbc.Driver";
	//public static String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	public static String dbUrl = "jdbc:mysql://localhost:3306/dblp?serverTimezone=UTC";
	//public static String dbUrl = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=dblp;zeroDateTimeBehavior=convertToNull";
	public static String user = "root";
	public static String password = "";

	public static Connection getConn() {
		try {
			Class.forName(driverName);
			return DriverManager.getConnection(dbUrl, user, password);
		} catch (Exception e) {
			System.out.println("Error while opening a conneciton to database server: " + e.getMessage());
			return null;
		}
	}
}
