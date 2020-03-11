package service;

import java.util.List;

import model.Company;
import model.Computer;

public class Pageable {
	public static List<Computer> getComputerPage(int currentOffset, int computersPerPage){
		currentOffset += computersPerPage; 
		return ComputerValidator.findListWithOffset(currentOffset, computersPerPage);
	}
	
	public static List<Company> getCompanyPage(int currentOffset, int companiesPerPage){
		return CompanyValidator.fetchWithOffset(currentOffset, companiesPerPage);
	}
}
