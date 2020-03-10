package service;

import java.util.List;
import java.util.Optional;

import model.Company;
import persistence.CompanySearcher;

public class CompanyValidator {
	/**
	 * Recherche d'une entreprise à partir de son identifiant
	 * @param id l'identifiant recherché
	 * @return une instance de Optional contenant une instance de Company si une ligne correspondante a été trouvée dans la BD
	 *  ou une instance de Optional vide si aucune entreprise n'a été trouvée
	 */
	public static Optional<Company> findCompanyById(long id){
		return CompanySearcher.fetchCompanyById(id);
	}
	
	/**
	 * Fonction renvoyant la liste des entreprises présentes dans la BD
	 * @return La liste des entreprises présentes dans la BD
	 */
	public static List<Company> findCompaniesList(){
		return CompanySearcher.fetchCompanies();
	}
}
