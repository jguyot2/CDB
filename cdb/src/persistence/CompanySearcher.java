package persistence;

import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import model.Company;

/**
 * Classe utilisée pour les requêtes associées à la table company.
 * 
 * @author jguyot2
 *
 */
public class CompanySearcher {
	private static final String REQUEST_COMPANIES = "SELECT name, id FROM company";
	private static final String REQUEST_SEARCH_BY_ID = "SELECT name, id FROM company WHERE id = ?";

	/**
	 * Recherche une compagnie par son identifiant dans la base de données.
	 * 
	 * @param searchedId l'identifiant de l'entreprise identifiée
	 * @return Optional.empty() si aucune entreprise n'a été trouvée, ou une instance de Optional 
	 * contenant l'entreprise trouvée sinon
	 */
	public static Optional<Company> fetchCompanyById(long searchedId) {
		try ( PreparedStatement stmt = DBConnection.getConnection().prepareStatement(REQUEST_SEARCH_BY_ID)) {
			
			stmt.setLong(1, searchedId);
	
			ResultSet res = stmt.executeQuery();

			if (!res.next())
				return Optional.empty();

			Integer companyId = new Integer(res.getInt("id"));
			String  companyName = res.getString("name");
			
			Company foundCompany = new Company(companyName, companyId);
			return Optional.of(foundCompany);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	/**
	 * Rend la liste des entreprises présentes dans la base de données, sous la forme d'instance de Company
	 * @return La liste des entreprises présentes dans la base de données
	 */
	public static List<Company> fetchCompanies() {
		
		List<Company> companiesList = new ArrayList<>();
		
		try (Statement stmt = DBConnection.getConnection().createStatement()){
			ResultSet res = stmt.executeQuery(REQUEST_COMPANIES);
			while (res.next()) {
				long id = res.getLong("id");
				String name = res.getString("name");
				assert (name != null);
				companiesList.add(new Company(name, id));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return companiesList;
	}

}
