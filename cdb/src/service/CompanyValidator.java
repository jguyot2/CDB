package service;

import java.util.List;
import java.util.Optional;

import model.Company;
import persistence.CompanySearcher;

public class CompanyValidator {
	public static Optional<Company> findCompanyById(long id){
		return CompanySearcher.fetchCompanyById(id);
	}
	
	public static List<Company> findCompaniesList(){
		return CompanySearcher.fetchCompanies();
	}
}
