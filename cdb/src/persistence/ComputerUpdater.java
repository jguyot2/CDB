package persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import model.Computer;

public class ComputerUpdater {
	private static final String DELETE_COMPUTER = "DELETE FROM computer WHERE id = ?";
	private static final String UPDATE_COMPUTER = "UPDATE computer SET "
			+ "name = ?, introduced = ?, discontinued = ?, company_id = ?" + "WHERE id = ?";

	private static final String CREATE_COMPUTER = 
			"INSERT INTO computer(name, introduced, discontinued, company_id) VALUES ?, ?, ?, ?";
	public static boolean deleteComputerById(long id) {
		try {
			PreparedStatement stmt = DBConnection.getConnection().prepareStatement(DELETE_COMPUTER);
			stmt.setLong(1, id);
			int deleted_lines = stmt.executeUpdate();
			if (deleted_lines == 0)
				return false;
			else
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static java.sql.Date utilDateToSqlDate(Date date) {
		return new java.sql.Date(date.getTime());
	}

	public static boolean updateComputerById(long id, Computer newComputer) {
		try {
			PreparedStatement stmt = DBConnection.getConnection().prepareStatement(UPDATE_COMPUTER);
			stmt.setLong(1, id);
			
			stmt.setString(2, newComputer.getName());
			stmt.setDate(3, utilDateToSqlDate(newComputer.getIntroduction()));
			stmt.setDate(4, utilDateToSqlDate(newComputer.getDiscontinuation()));
			if (newComputer.getManufacturer() == null)
				stmt.setNull(5, java.sql.Types.BIGINT);
			else
				stmt.setLong(5, newComputer.getManufacturer().getId());
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean createComputer(Computer newComputer) {
		try {
			PreparedStatement stmt = DBConnection.getConnection().prepareStatement(CREATE_COMPUTER);
			stmt.setString(1, newComputer.getName());
			stmt.setDate(2, utilDateToSqlDate(newComputer.getIntroduction()));
			stmt.setDate(3, utilDateToSqlDate(newComputer.getDiscontinuation()));
			if(newComputer.getManufacturer() == null) 
				stmt.setNull(4, java.sql.Types.BIGINT);
			 else
				stmt.setLong(4, newComputer.getManufacturer().getId());
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
