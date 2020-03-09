package persistence;

import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import model.Company;

public class CompanySearcher {
	private static final String REQUEST_COMPANIES = "SELECT name, id FROM company";
	private static final String REQUEST_SEARCH_BY_ID = "SELECT name, id FROM company WHERE id = ?";

	public static Optional<Company> fetchCompanyById(long searchedId) throws NotFoundException, SQLException {
		PreparedStatement stmt = DBConnection.getConnection().prepareStatement(REQUEST_SEARCH_BY_ID);
		stmt.setLong(1, searchedId);
		ResultSet res = stmt.executeQuery();

		if (!res.next())
			return Optional.empty();

		Integer companyId = new Integer(res.getInt("id"));
		String companyName = res.getString("name");
		Company foundCompany = new Company(companyName, companyId);
		return Optional.of(foundCompany);
	}
	
	public static List<Company> fetchCompanies(){
		List<Company> retour = new ArrayList<>();
		return retour;
	}

}
