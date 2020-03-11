package persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Computer;
import mapper.DateMapper;

/**
 * Classe permettant d'effectuer des mises à jour sur les éléments de la table
 * computer à partir d'instances de Computer
 * 
 * @author jguyot2
 */
public class ComputerUpdater {
	private static final String DELETE_COMPUTER = "DELETE FROM computer WHERE id = ?";
	private static final String UPDATE_COMPUTER = "UPDATE computer SET "
			+ "name = ?, introduced = ?, discontinued = ?, company_id = ?" + "WHERE id = ?";

	private static final String CREATE_COMPUTER = "INSERT INTO computer(name, introduced, discontinued, company_id) VALUES (?, ?, ?, ?)";

	/**
	 * Supprime un ordinateur donné de la base à partir de son identifiant
	 * 
	 * @param id l'identifiant dans la base de l'ordinateur à supprimer
	 * @return true si la suppression est effective, false si une erreur a eu lieu
	 */
	public static int deleteComputerById(long id) {
		try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(DELETE_COMPUTER)) {
			stmt.setLong(1, id);
			return stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Met à jour l'ordinateur possédant l'identifiant en paramètre pour le faire
	 * correspondre à l'ordinateur en paramètre
	 *
	 * @param id          l'identifiant de l'ordinateur à modifier
	 * @param newComputer la valeur de l'ordinateur à laquelle on veut faire
	 *                    correspondre la ligne
	 * @return 1 si la mise à jour a eu lieu, 0 sinon 
	 * 
	 * 
	 */
	public static int updateComputer(Computer newComputer) {
		long id = newComputer.getId();
		try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(UPDATE_COMPUTER)) {
			stmt.setLong(1, id);
			stmt.setString(2, newComputer.getName());
			stmt.setDate(3, DateMapper.utilDateToSqlDate(newComputer.getIntroduction()));
			stmt.setDate(4, DateMapper.utilDateToSqlDate(newComputer.getDiscontinuation()));
			
			if (newComputer.getManufacturer() == null)
				stmt.setNull(5, java.sql.Types.BIGINT);
			else
				stmt.setLong(5, newComputer.getManufacturer().getId());
			return stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Ajoute une ligne à la base de données, correspondant à l'instance de Computer donnée en paramètre
	 * @param newComputer l'instance de Computer à enregistrer dans la base de données
	 * @return le nouvel identifiant correspondant à la ligne ajoutée si l'ajout a réussi, 0 si l'ajout a raté
	 */
	public static long createComputer(Computer newComputer) {
		try (PreparedStatement stmt = 
				DBConnection.getConnection()
							.prepareStatement(CREATE_COMPUTER, PreparedStatement.RETURN_GENERATED_KEYS))
		{
			stmt.setString(1, newComputer.getName());
			stmt.setDate(2, DateMapper.utilDateToSqlDate(newComputer.getIntroduction()));
			stmt.setDate(3, DateMapper.utilDateToSqlDate(newComputer.getDiscontinuation()));
			
			if (newComputer.getManufacturer() == null)
				stmt.setNull(4, java.sql.Types.BIGINT);
			else
				stmt.setLong(4, newComputer.getManufacturer().getId());
			stmt.executeUpdate();
			ResultSet keySet = stmt.getGeneratedKeys();
			if (!keySet.next())
				return 0;
			return keySet.getLong(1);
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.print(e.getMessage());
		}
		return 0;
	}

	private ComputerUpdater() {}

}
