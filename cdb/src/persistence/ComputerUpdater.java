package persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ComputerUpdater {
	private static final String DELETE_COMPUTER = "DELETE FROM computer WHERE id = ?";

	public boolean deleteComputerById(long id) {
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
}
