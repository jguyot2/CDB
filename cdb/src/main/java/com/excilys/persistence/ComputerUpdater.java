package com.excilys.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.mapper.DateMapper;
import com.excilys.model.Computer;

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

	private static final Logger logger = LoggerFactory.getLogger(ComputerUpdater.class);
	

	public int deleteById(long id) throws SQLException {
		logger.info("Suppression du pc d'id : " + id);
		try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(DELETE_COMPUTER)) {
			stmt.setLong(1, id);
			return stmt.executeUpdate();
		}
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
	public int updateComputer(Computer newComputer) throws SQLException {
		logger.info("Mise à jour de l'instance de Computer suivante " + newComputer.toString());
		long id = newComputer.getId();
		
		try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(UPDATE_COMPUTER)) {
			stmt.setLong(1, id);
			stmt.setString(2, newComputer.getName());

			Optional<java.sql.Date> introDateOpt = DateMapper.localDateToSqlDate(newComputer.getIntroduction());
			java.sql.Date introDate = null;
			if (introDateOpt.isPresent())
				introDate = introDateOpt.get();
			stmt.setDate(3, introDate);

			Optional<java.sql.Date> discoDateOpt = DateMapper.localDateToSqlDate(newComputer.getDiscontinuation());
			java.sql.Date discoDate = null;
			if (discoDateOpt.isPresent())
				discoDate = discoDateOpt.get();
			stmt.setDate(4, discoDate);

			if (newComputer.getManufacturer() == null)
				stmt.setNull(5, java.sql.Types.BIGINT);
			else
				stmt.setLong(5, newComputer.getManufacturer().getId());
			return stmt.executeUpdate();
		}
	}

	/**
	 * Ajoute une ligne à la base de données, correspondant à l'instance de Computer
	 * donnée en paramètre
	 * 
	 * @param newComputer l'instance de Computer à enregistrer dans la base de
	 *                    données
	 * @return le nouvel identifiant correspondant à la ligne ajoutée si l'ajout a
	 *         réussi, 0 si l'ajout a raté
	 */
	public long createComputer(Computer newComputer) throws SQLException {
		logger.info("Création de l'instance de Computer suivante: " + newComputer);
		try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(CREATE_COMPUTER,
				PreparedStatement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, newComputer.getName());

			Optional<java.sql.Date> introDateOpt = DateMapper.localDateToSqlDate(newComputer.getIntroduction());
			java.sql.Date introDate = null;
			if (introDateOpt.isPresent())
				introDate = introDateOpt.get();
			stmt.setDate(2, introDate);

			Optional<java.sql.Date> discoDateOpt = DateMapper.localDateToSqlDate(newComputer.getDiscontinuation());
			java.sql.Date discoDate = null;
			if (discoDateOpt.isPresent())
				discoDate = discoDateOpt.get();
			stmt.setDate(3, discoDate);

			if (newComputer.getManufacturer() == null)
				stmt.setNull(4, java.sql.Types.BIGINT);
			else
				stmt.setLong(4, newComputer.getManufacturer().getId());

			stmt.executeUpdate();

			ResultSet keySet = stmt.getGeneratedKeys();
			if (!keySet.next()) {
				logger.error("Pas de PC créé");
				return 0;
			}
			return keySet.getLong(1);
		} 
	}

	public ComputerUpdater() {
	}


}