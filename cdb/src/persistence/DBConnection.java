package persistence;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

/**
 * Classe permettant de récupérer la connexion à la base de données. 
 * @author jguyot2
 *
 */
class DBConnection {

	private static Connection conn;
	private static boolean driverIsImported = false; 

	private static final String driverName = "com.mysql.cj.jdbc.Driver";
	private static final String urlDB = "jdbc:mysql://localhost:3306/";
	private static final String dbName = "computer-database-db";
	
	private static final String username = "admincdb";
	private static final String password = "qwerty1234";
	
	private DBConnection() {}

	/**
	 * Import du driver, à appeller une fois au début du programme
	 */
	private static void init() {
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
		driverIsImported = true;
	}

	/**
	 * Fonction pour récupérer la connexion. Si la connexion
	 * n'est pas initialisée ou est fermée, une nouvelle
	 * connexion est ouverte.
	 * @return la connexion à la base de donnée
	 */
	public static Connection getConnection() {
		if (!driverIsImported)
			init();

		try {
			if (conn == null || conn.isClosed())
				conn = DriverManager.getConnection(urlDB + dbName, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
}
