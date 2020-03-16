package com.excilys.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.model.Company;
import com.excilys.model.Page;
import com.excilys.persistence.CompanySearcher;

public class CompanyValidator {
	private Logger logger = LoggerFactory.getLogger(CompanyValidator.class);
	
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
			logger.debug("findById" + e.getMessage(), e);
			return Optional.empty();
		}
	}

	/**
	 * Fonction renvoyant la liste des entreprises présentes dans la BD
	 * @return La liste des entreprises présentes dans la BD
	 */
	public List<Company> fetchList() {
		try {
			return companySearcher.fetchList();
		} catch (SQLException e) {
			logger.debug("fetchList: " + e.getMessage(), e);
			return new ArrayList<>();
		}
	}

	public int getNunberOfElements() {
		try {
			return companySearcher.getNumberOfElements();
		} catch (SQLException e) {
			logger.error("getNbOfElements : " + e.getMessage(), e);
			return -1; // TODO : Gestion propre de ce cas
		}
	}

	public List<Company> fetchWithOffset(Page page) {
		try {
			return companySearcher.fetchWithOffset(page);
		} catch (SQLException e) {
			logger.error("Recherche de la liste : Aucun élément retourné");
			return new ArrayList<>(); // TODO : gestion propre de ce cas
		}
	}

}
