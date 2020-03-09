package persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import model.Company;
import model.Computer;


/**
 * Classe faisant l'interface entre 
 * @author jguyot2
 *
 */
public class ComputerSearcher {
	private static final String QUERY_COMPUTERS_WITHOUT_COMPANIES = 
			"SELECT id, name, introduced, discontinued "
			+ "FROM computer WHERE computer.company_id is NULL";

	private static final String QUERY_COMPUTERS_WITH_COMPANIES = 
			"SELECT company.id as company_id, company.name as company_name, "
			+ "computer.id as computer_id, computer.name as computer_name, " 
			+ "introduced, discontinued "
			+ "FROM computer, company WHERE computer.company_id = company.id";

	private static final String QUERY_COMPUTER_FROM_ID = 
			"SELECT id, name, introduced, discontinued, company_id "
			+ "FROM computer " + "WHERE computer.id = ? ";

	/**
	 * Ajoute la liste des ordinateurs de la base qui ne possèdent pas d'attributs
	 * "company" dans la liste
	 * 
	 * @param out  La liste dans laquelle il faut ajouter les éléments
	 * @param stmt le Statement permettant de faire les requêtes
	 */
	private static void addComputersWithoutCompanyToList(List<Computer> out, Statement stmt) {
		try {
			ResultSet computersWithoutCompanies = stmt.executeQuery(QUERY_COMPUTERS_WITHOUT_COMPANIES);
			while (computersWithoutCompanies.next()) {
				String name = computersWithoutCompanies.getString("name");
				Company company = null;
				Date introduced = computersWithoutCompanies.getDate("introduced");
				Date discontinued = computersWithoutCompanies.getDate("discontinued");
				long id = computersWithoutCompanies.getLong("id");
				Computer computer = new Computer(name, company, introduced, discontinued, id);
				out.add(computer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ajoute la liste des ordinateurs de la base qui possèdent un attribut
	 * "company" dans la liste
	 * 
	 * @param out  La liste dans laquelle il faut ajouter les éléments
	 * @param stmt le Statement permettant de faire les requêtes
	 */
	private static void addComputersWithCompanyToList(List<Computer> out, Statement stmt) {
		try {
			ResultSet computersWithCompanies = stmt.executeQuery(QUERY_COMPUTERS_WITH_COMPANIES);
			while (computersWithCompanies.next()) {
				String computerName = computersWithCompanies.getString("computer_name");

				String companyName = computersWithCompanies.getString("company_name");
				long companyId = computersWithCompanies.getLong("company_id");
				Company company = new Company(companyName, companyId);

				Date introduced = computersWithCompanies.getDate("introduced");
				Date discontinued = computersWithCompanies.getDate("discontinued");
				long computerId = computersWithCompanies.getLong("computer_id");
				Computer computer = new Computer(computerName, company, introduced, discontinued, computerId);

				out.add(computer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cherche tous les ordinateurs dans la base, et retourne la liste correspondant
	 * à ces derniers
	 * 
	 * @return La liste des ordinateurs présents dans la base de données
	 * @author jguyot2
	 */
	public static List<Computer> fetchComputerList() {
		List<Computer> computerList = new ArrayList<>();
		Statement stmt;
		try {
			stmt = DBConnection.getConnection().createStatement();
			addComputersWithoutCompanyToList(computerList, stmt);
			addComputersWithCompanyToList(computerList, stmt);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return computerList;
	}
/**
 * Recherche un ordinateur dans la base de donnée, à partir d'un identifiant
 * @param searchedId L'id de l'ordinateur recherché
 * @return une instance de Optional vide si aucun ordinateur de l'id donné n'est présent dans la BD ou qu'une
 * exception SQLException s'est produite
 */
	public static Optional<Computer> fetchComputerById(long searchedId) {
		try {
			PreparedStatement stmt = DBConnection.getConnection().prepareStatement(QUERY_COMPUTER_FROM_ID);

			stmt.setLong(1, searchedId);
			ResultSet res = stmt.executeQuery();

			if (!res.next()) {
				return Optional.empty();
			}
				
			String name = res.getString("name");

			Company company = null;
			long company_id = res.getLong("company_id");
			if (!res.wasNull()) {
				Optional<Company> opt_company = CompanySearcher.fetchCompanyById(company_id);
				company = opt_company.orElse(null);
			}
			Date introduced = res.getDate("introduced");
			Date discontinued = res.getDate("discontinued");
			long id = res.getLong("id");

			return Optional.of(new Computer(name, company, introduced, discontinued, id));
		} catch (SQLException e) {
			e.printStackTrace();
			return Optional.empty();
		} catch (NotFoundException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	private ComputerSearcher() {
	}

}
