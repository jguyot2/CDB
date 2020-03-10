package service;

import java.util.List;
import java.util.Optional;

import model.Company;
import persistence.CompanySearcher;

public class CompanyValidator {
	public Optional<Company> findCompanyById(long id){
		return CompanySearcher.fetchCompanyById(id);
	}
	
	public List<Company> findCompaniesList(long id){
		return CompanySearcher.fetchCompanies();
	}
}
