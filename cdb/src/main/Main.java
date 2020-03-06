package main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import persistence.DBConnection;

public class Main {
	public static void main(String...strings) {
		Connection conn = DBConnection.getConnection();
		System.out.println("pouet");
		try {
			Statement stmt = conn.createStatement();
			ResultSet res = stmt.executeQuery("SELECT * FROM computer");
			if(res.next())
				System.out.println(res.getString("Name"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
