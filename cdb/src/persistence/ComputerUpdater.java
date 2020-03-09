package persistence;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import model.Computer;

/**
 * Classe permettant d'effectuer des mises à jour sur les éléments de la table computer à partir 
 * d'instances de Computer
 * @author jguyot2
 *
 */
public class ComputerUpdater {
	private static final String DELETE_COMPUTER = "DELETE FROM computer WHERE id = ?";
	private static final String UPDATE_COMPUTER = "UPDATE computer SET "
			+ "name = ?, introduced = ?, discontinued = ?, company_id = ?" + "WHERE id = ?";

	private static final String CREATE_COMPUTER = 
			"INSERT INTO computer(name, introduced, discontinued, company_id) VALUES ?, ?, ?, ?";
	
	/**
	 * Supprime un ordinateur donné de la base à partir de son identifiant
	 * @param id l'identifiant dans la base de l'ordinateur à supprimer
	 * @return true si la suppression est effective, false si une erreur a eu lieu
	 */
	public static boolean deleteComputerById(long id) {
		try {
			PreparedStatement stmt = DBConnection.getConnection().prepareStatement(DELETE_COMPUTER);
			stmt.setLong(1, id);
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Fonction de conversion d'une instance de java.util.date vers une instance de java.sql.Date
	 * @param date La date à convertir
	 * @return La date correspondant au paramètre, mais qui est une instance de java.sql.Date
	 */
	public static java.sql.Date utilDateToSqlDate(Date date) {
		return new java.sql.Date(date.getTime());
	}

	/**
	 * Met à jour l'ordinateur possédant l'identifiant en paramètre pour le faire correspondre 
	 * à l'ordinateur en paramètre
	 *
	 * @param id l'identifiant de l'ordinateur à modifier
	 * @param newComputer la valeur de l'ordinateur à laquelle on veut faire correspondre la ligne
	 * @return true si la mise à jour a eu lieu, false sinon
	 */
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
	
	/**
	 * Création d'un ordinateur dans la BD
	 * @param newComputer l'ordinateur à ajouter dans la base
	 * @return true si la création a eu lieu, false sinon
	 */
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
	
	private ComputerUpdater() {
	}
	
}
