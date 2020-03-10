package persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import model.Company;
import model.Computer;

/**
 * Classe permettant d'effectuer des requêtes sur des Computers
 * 
 * @author jguyot2
 *
 */
public class ComputerSearcher {
	// Pê regarder le Outer Join pour avoir à faire une seule requête

	private static final String QUERY_COMPUTER_LIST = " SELECT computer.id, computer.name, introduced, discontinued, "
			+ "company.id, company.name " + "FROM computer LEFT JOIN company " + "ON computer.company_id = company.id";
	
	private static final String QUERY_COMPUTER_FROM_ID = "SELECT id, name, introduced, discontinued, company_id "
			+ "FROM computer " + "WHERE computer.id = ? ";

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
			ResultSet res = stmt.executeQuery(QUERY_COMPUTER_LIST);
			// TODO: une hashMap pour créer moins d'instances de Company
			while (res.next()) {
				Computer computer = getComputerFromResultSet(res);
				computerList.add(computer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return computerList;
	}

	private static Computer getComputerFromResultSet(ResultSet res) throws SQLException {
		long computerId = res.getLong("computer.id");
		String computerName = res.getString("computer.name");
		Date introduced = res.getDate("introduced");
		Date discontinued = res.getDate("discontinued");
		
		String companyName = res.getString("company.name");
		Company company = null;

		if (companyName != null) {
			long companyId = res.getLong("company.id");
			company = new Company(companyName, companyId);
		}

		Computer computer = new Computer(computerName, company, introduced, discontinued, computerId);

		return computer;
	}

	/**
	 * Recherche un ordinateur dans la base de donnée, à partir d'un identifiant
	 * 
	 * @param searchedId L'id de l'ordinateur recherché
	 * @return une instance de Optional vide si aucun ordinateur de l'id donné n'est
	 *         présent dans la BD ou qu'une exception SQLException s'est produite
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
		}
	}

	private ComputerSearcher() {}

}
