package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.Computer;
import model.Pagination;
import persistence.ComputerSearcher;
import persistence.ComputerUpdater;

/**
 * Classe validant les requêtes/mises à jour avant de les envoyer au paquet
 * persistance
 * 
 * @author jguyot2
 *
 */
public class ComputerValidator {
	ComputerSearcher computerSearcher;
	ComputerUpdater computerUpdater;

	public ComputerValidator() {
		this.computerSearcher = new ComputerSearcher();
		this.computerUpdater = new ComputerUpdater();
	}

	public int getNumberOfElements() {
		try {
			return computerSearcher.getNumberOfElements();
		} catch (SQLException e) {
			return -1;
		}
	}

	public List<ComputerInstanceProblems> getComputerInstanceProblems(Computer computer) {
		List<ComputerInstanceProblems> problems = new ArrayList<>();

		if (computer.getName() == null || computer.getName().trim().isEmpty())
			problems.add(ComputerInstanceProblems.INVALID_NAME);

		if (computer.getIntroduction() != null)
			if (computer.getDiscontinuation() != null
					&& computer.getIntroduction().compareTo(computer.getDiscontinuation()) < 0)
				problems.add(ComputerInstanceProblems.INVALID_DISCONTINUATION_DATE);

		if (computer.getIntroduction() == null && computer.getDiscontinuation() != null)
			problems.add(ComputerInstanceProblems.NULL_INTRO_WITH_NOT_NULL_DISCONTINUATION);
		return problems;
	}

	public int updateComputer(Computer newComputervalue) throws InvalidComputerInstanceException {
		List<ComputerInstanceProblems> problems = getComputerInstanceProblems(newComputervalue);
		if (problems.size() > 0)
			throw new InvalidComputerInstanceException(problems);
		try {
			return computerUpdater.updateComputer(newComputervalue);
		} catch (SQLException e) {
			return -1;
		}
	}

	public int deleteComputer(long id) {
		try {
			return computerUpdater.deleteComputerById(id);
		} catch (SQLException e) {
			return -1;
		}
	}

	public long createComputer(Computer createdComputer) throws InvalidComputerInstanceException {
		List<ComputerInstanceProblems> problems = getComputerInstanceProblems(createdComputer);
		if (problems.size() > 0)
			throw new InvalidComputerInstanceException(problems);
		try {

			return computerUpdater.createComputer(createdComputer);
		} catch (SQLException e) {

			return 0;
		}
	}

	/**
	 * Recherche d'une instance de Computer dans base
	 * 
	 * @param id l'identifiant du computer recherché dans la base de donné
	 * @return Optional.empty() si la recherche a échoué, ou un Optional contenant
	 *         la valeur de l'identifiant recherché sinon
	 */
	public Optional<Computer> findById(long id) {
		try {
			return computerSearcher.fetchById(id);
		} catch (SQLException e) {
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
			return new ArrayList<>();
		}
	}

	public List<Computer> findListWithOffset(Pagination page) {
		try {
			return computerSearcher.fetchWithOffset(page);
		} catch (SQLException e) {
			return new ArrayList<>();
		}
	}
}
