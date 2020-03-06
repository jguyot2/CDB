package persistence;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class DBConnection {

	private static Connection conn;
	private static boolean initialized = false;

	private static final String driverName = "com.mysql.cj.jdbc.Driver";
	private static final String password = "qwerty1234";
	private static final String urlDB = "jdbc:mysql://localhost:3306/";
	private static final String dbName = "computer-database-db";
	private static final String username = "admincdb";

	private DBConnection() {
	}

	private static void init() {
		// TODO: Pê virer ça parce que c'est con de l'évaluer plusieurs fois
		try {
			Class.forName(driverName).newInstance();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		initialized = true;
	}

	public static Connection getConnection() {
		if (!initialized)
			init();

		try {
			if (conn == null || conn.isClosed())
				conn = DriverManager.getConnection(urlDB + dbName, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
			// Throw exception ? 
		}
		return conn;
	}
}
