package service;

import java.util.List;
import java.util.Optional;

import model.Company;
import model.Pagination;
import persistence.CompanySearcher;

public class CompanyValidator {
	
	private CompanySearcher companySearcher;
	public CompanyValidator() {
		companySearcher = new CompanySearcher();
	}
	/**
	 * Recherche d'une entreprise à partir de son identifiant
	 * @param id l'identifiant recherché
	 * @return une instance de Optional contenant une instance de Company si une ligne correspondante a été trouvée dans la BD
	 *  ou une instance de Optional vide si aucune entreprise n'a été trouvée
	 */
	public  Optional<Company> findById(long id){
		return companySearcher.fetchById(id);
	}
	
	/**
	 * Fonction renvoyant la liste des entreprises présentes dans la BD
	 * @return La liste des entreprises présentes dans la BD
	 */
	public List<Company> fetchList(){
		return companySearcher.fetchList();
	}
	public int getNunberOfElements() {
		return companySearcher.getNumberOfElements();
	}
	public List<Company> fetchWithOffset(Pagination page){
		return companySearcher.fetchWithOffset(page);
	}
	
}
