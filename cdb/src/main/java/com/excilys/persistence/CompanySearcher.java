package com.excilys.persistence;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.model.Company;
import com.excilys.model.Page;

/**
 * Classe utilisée pour les requêtes associées à la table company.
 * 
 * @author jguyot2
 *
 */
public class CompanySearcher implements Searcher<Company> {
	private static final Logger logger = LoggerFactory.getLogger(CompanySearcher.class);
	
	private static final String REQUEST_COMPANIES = "SELECT name, id FROM company";
	private static final String REQUEST_SEARCH_BY_ID = "SELECT name, id FROM company WHERE id = ?";
	private static final String REQUEST_COMPANIES_OFFSET = "SELECT name, id FROM company ORDER BY id LIMIT ? OFFSET ?";
	private static final String REQUEST_NB_OF_ROWS = "SELECT count(id) FROM company";
	
	public CompanySearcher() {}
	
	/**
	 * Recherche une compagnie par son identifiant dans la base de données.
	 * 
	 * @param searchedId l'identifiant de l'entreprise identifiée
	 * @return Optional.empty() si aucune entreprise n'a été trouvée, ou une
	 *         instance de Optional contenant l'entreprise trouvée sinon
	 */
	public Optional<Company> fetchById(long searchedId) throws SQLException {
		try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(REQUEST_SEARCH_BY_ID)) {
			stmt.setLong(1, searchedId);
			ResultSet res = stmt.executeQuery();
			if (!res.next()) {
				logger.debug("fetchById: La recherche n'a donné aucun résultat pour l'id" + searchedId);
				return Optional.empty();
			}
			Integer companyId = new Integer(res.getInt("id"));
			String companyName = res.getString("name");
			Company foundCompany = new Company(companyName, companyId);
			return Optional.of(foundCompany);
		}
	}

	/**
	 * Rend la liste des entreprises présentes dans la base de données, sous la
	 * forme d'instance de Company
	 * 
	 * @return La liste des entreprises présentes dans la base de données
	 */
	public List<Company> fetchList() throws SQLException {
		List<Company> companiesList = new ArrayList<>();
		try (Statement stmt = DBConnection.getConnection().createStatement()) {
			ResultSet res = stmt.executeQuery(REQUEST_COMPANIES);
			while (res.next()) {
				long id = res.getLong("id");
				String name = res.getString("name");
				assert (name != null);
				companiesList.add(new Company(name, id));
			}
		}
		return companiesList;
	}

	/**
	 *  Recherche une "page" de la liste des entreprises, en fonction du paramètre
	 */
	public List<Company> fetchWithOffset(Page page) throws SQLException {
		List<Company> ret = new ArrayList<>();
		try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(REQUEST_COMPANIES_OFFSET)) {
			stmt.setInt(1, page.getElemeentsperpage());
			stmt.setInt(2, page.getOffset());
			ResultSet res = stmt.executeQuery();
			while (res.next()) {
				long companyId = res.getLong("id");
				String companyName = res.getString("name");
				ret.add(new Company(companyName, companyId));
			}
		}
		return ret;
	}
	
	/**
	 * renvoie la taille de la table
	 */
	public int getNumberOfElements() throws SQLException {
		try (Statement stmt = DBConnection.getConnection().createStatement()){
			ResultSet res = stmt.executeQuery(REQUEST_NB_OF_ROWS);
			if(res.next())
				return res.getInt(1);
			logger.error("La récupération du nombre d'éléments a raté");
			return -1; // TODO : Gestion de ce cas 
		}
	}
}
