package com.excilys.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.model.Computer;
import com.excilys.model.Page;
import com.excilys.persistence.ComputerSearcher;
import com.excilys.persistence.ComputerUpdater;

/**
 * Classe validant les requêtes/mises à jour avant de les envoyer au paquet
 * persistance
 * 
 * @author jguyot2
 */
public class ComputerValidator implements SearchValidator<Computer> {
	private static final Logger logger = LoggerFactory.getLogger(ComputerValidator.class);

	private ComputerSearcher computerSearcher;
	private ComputerUpdater computerUpdater;

	public ComputerValidator() {
		this.computerSearcher = new ComputerSearcher();
		this.computerUpdater = new ComputerUpdater();
	}
	
	public ComputerValidator(ComputerSearcher computerSearcher, ComputerUpdater computerUpdater) {
		this.computerSearcher = computerSearcher;
		this.computerUpdater = computerUpdater;
	}
	
	public void setComputerSearcher(ComputerSearcher newComputerSearcher, ComputerUpdater newComputerUpdater) {
		this.computerSearcher = newComputerSearcher;
		this.computerUpdater = newComputerUpdater;
	}
	
	public int getNumberOfElements() {
		try {
			return computerSearcher.getNumberOfElements();
		} catch (SQLException e) {
			logger.error("getNumberOfElements : " + e.getMessage(), e);
			return -1;
		}
	}

	private List<ComputerInstanceProblems> getComputerInstanceProblems(Computer computer) {
		List<ComputerInstanceProblems> problems = new ArrayList<>();

		if (computer.getName() == null || computer.getName().trim().isEmpty())
			problems.add(ComputerInstanceProblems.INVALID_NAME);

		if (computer.getIntroduction() != null)
			if (computer.getDiscontinuation() != null
					&& computer.getIntroduction().compareTo(computer.getDiscontinuation()) > 0)
				problems.add(ComputerInstanceProblems.INVALID_DISCONTINUATION_DATE);

		if (computer.getIntroduction() == null && computer.getDiscontinuation() != null)
			problems.add(ComputerInstanceProblems.NULL_INTRO_WITH_NOT_NULL_DISCONTINUATION);
		return problems;
	}

	/**
	 * Mise à jour de l'instance de Computer donnée
	 * 
	 * @param newComputervalue la nouvelle valeur de l'instance
	 * @return -1 si exception lors de la requête, 0 si pas de mise à jour, ou 1 si
	 *         la mise à jour a eu lieu
	 * @throws InvalidComputerInstanceException Si l'instance en paramètre n'est pas
	 *                                          valide
	 */
	public int updateComputer(Computer newComputervalue) throws InvalidComputerInstanceException {
		List<ComputerInstanceProblems> problems = getComputerInstanceProblems(newComputervalue);

		if (problems.size() > 0) {
			logger.debug("Mise à jour incorrecte du PC");
			throw new InvalidComputerInstanceException(problems);
		}

		try {
			return computerUpdater.updateComputer(newComputervalue);
		} catch (SQLException e) {
			logger.error("updateComputer :" + e.getMessage(), e);
			return -1; // TODO: gestion propre des erreurs ici
		}
	}

	public int deleteComputer(long id) {
		try {
			return computerUpdater.deleteById(id);
		} catch (SQLException e) {
			logger.error("deleteComputer :" + e.getMessage(), e);
			return -1; // TODO : gestion propre des erreurs
		}
	}

	/**
	 * 
	 * @param createdComputer
	 * @return 0 si la création n'a pas pu se faire dans la BD, ou le nouvel
	 *         identifiant qui vient d'être créé
	 * @throws InvalidComputerInstanceException si l'instance en paramètre
	 */
	public long createComputer(Computer createdComputer) throws InvalidComputerInstanceException {
		List<ComputerInstanceProblems> problems = getComputerInstanceProblems(createdComputer);
		if (problems.size() > 0) {
			logger.debug("createComputer : instance invalide");
			throw new InvalidComputerInstanceException(problems);
		}

		try {
			return computerUpdater.createComputer(createdComputer);
		} catch (SQLException e) {
			logger.error("createComputer :" + e.getMessage(), e);
			return 0;
		}
	}

	/**
	 * Recherche d'une instance de Computer dans la base
	 * 
	 * @param id l'identifiant du computer recherché dans la base de donné
	 * @return Optional.empty() si la recherche a échoué, ou un Optional contenant
	 *         la valeur de l'identifiant recherché sinon
	 */
	public Optional<Computer> findById(long id) {
		try {
			return computerSearcher.fetchById(id);
		} catch (SQLException e) {
			logger.error("findbyId(id = " + id + "): " + e.getMessage(), e);
			return Optional.empty();
		}
	}

	/**
	 * Recherche de la liste de tous les ordinateurs présent dans la base de données
	 * 
	 * @return La liste des ordinateurs présents dans la base de données
	 */
	public List<Computer> fetchList() {
		try {
			return computerSearcher.fetchList();
		} catch (SQLException e) {
			logger.error("fetchlist: " + e.getMessage(), e);
			return new ArrayList<>();
		}
	}

	public List<Computer> fetchWithOffset(Page page) {
		try {
			return computerSearcher.fetchWithOffset(page);
		} catch (SQLException e) {
			logger.error("fetchListWithOffset: " + e.getMessage(), e);
			return new ArrayList<>();
		}
	}
}
