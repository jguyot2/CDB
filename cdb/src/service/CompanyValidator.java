package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import model.Company;
import model.Page;
import persistence.CompanySearcher;

public class CompanyValidator {

	private CompanySearcher companySearcher;

	public CompanyValidator() {
		companySearcher = new CompanySearcher();
	}

	/**
	 * Recherche d'une entreprise à partir de son identifiant
	 * 
	 * @param id l'identifiant recherché
	 * @return une instance de Optional contenant une instance de Company si une
	 *         ligne correspondante a été trouvée dans la BD ou une instance de
	 *         Optional vide si aucune entreprise n'a été trouvée
	 */
	public Optional<Company> findById(long id) {
		try {
			return companySearcher.fetchById(id);
		} catch (SQLException e) {
			return Optional.empty();
		}
	}

	/**
	 * Fonction renvoyant la liste des entreprises présentes dans la BD
	 * 
	 * @return La liste des entreprises présentes dans la BD
	 */
	public List<Company> fetchList() {
		try {
			return companySearcher.fetchList();
		} catch (SQLException e) {
			return new ArrayList<>();
		}
	}

	public int getNunberOfElements() {
		try {
			return companySearcher.getNumberOfElements();
		} catch (SQLException e) {
			return -1;
		}
	}

	public List<Company> fetchWithOffset(Page page) {
		try {
			return companySearcher.fetchWithOffset(page);
		} catch (SQLException e) {
			return new ArrayList<>();
		}
	}

}
