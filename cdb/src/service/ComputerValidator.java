package service;

import java.util.List;
import java.util.Optional;

import model.Computer;
import model.Pagination;
import persistence.ComputerSearcher;
import persistence.ComputerUpdater;

/**
 * Classe validant les requêtes/mises à jour avant de les envoyer au paquet
 * persistance
 * 
 * @author jguyot2
 *
 */
public class ComputerValidator {
	ComputerSearcher computerSearcher;
	ComputerUpdater computerUpdater;

	public ComputerValidator() {
		this.computerSearcher = new ComputerSearcher();
		this.computerUpdater = new ComputerUpdater();
	}
	public int getNumberOfElements() {
		return computerSearcher.getNumberOfElements();
	}
	/**
	 * Fonction déterminant si une instance de Computer est valide, c'est à dire que
	 * -> Son nom n'est pas 'null' ou une chaîne de caractères vide -> Si elle a une
	 * date d'introduction ainsi qu'une date d'interruption, alors que
	 * l'introduction soit avant ou le même jour que l'interruption -> Si elle n'a
	 * pas de date d'introduction, alors il n'y a pas de date d'interruption.
	 * 
	 * @param computer l'instance de Computer à valider
	 * @return true si l'instance est considérée comme valide, false sinon
	 * 
	 *         TODO : Changer l'implémentation de cette fonction pour expliciter ce
	 *         qui pose problème dans l'instance
	 */
	public boolean isValidComputerInstance(Computer computer) {
		if (computer.getName() == null || computer.getName().trim().isEmpty())
			return false;

		if (computer.getIntroduction() != null) {
			if (computer.getDiscontinuation() == null)
				return true;
			return computer.getIntroduction().compareTo(computer.getDiscontinuation()) <= 0;
		}
		return computer.getDiscontinuation() == null;
	}

	/**
	 * Fonction permettant la mise à jour de la ligne d'identifiant id pour lui
	 * donner la valeur correspondant à l'instance de Computer en paramètre
	 * 
	 * @param id               l'identifiant de la ligne à modifier
	 * @param newComputervalue La valeur à affecter à la ligne
	 * @return true si la mise à jour a bien eu lieu, false sinon
	 *
	 */
	public int updateComputer(Computer newComputervalue) {
		if (!isValidComputerInstance(newComputervalue))
			return 0;
		else
			return computerUpdater.updateComputer(newComputervalue);
	}

	/**
	 * Fonction supprimant l'instance de Computer possédant la ligne id
	 * 
	 * @param id
	 * @return true si la suppression a eu lieu, false sinon
	 */
	public int deleteComputer(long id) {
		return computerUpdater.deleteComputerById(id);
	}

	/**
	 * Ajout d'un ordinateur dans la base de donnée
	 * 
	 * @param createdComputer l'instance de Computer à ajouter dans la base
	 * @return 0 si la création a échoué ou que , l'identifiant de la nouvelle ligne
	 *         créée dans la base sinon
	 */
	public long createComputer(Computer createdComputer) {
		if (!isValidComputerInstance(createdComputer))
			return 0;
		return computerUpdater.createComputer(createdComputer);
	}

	/**
	 * Recherche d'une instance de Computer dans base
	 * 
	 * @param id l'identifiant du computer recherché dans la base de donné
	 * @return Optional.empty() si la recherche a échoué, ou un Optional contenant
	 *         la valeur de l'identifiant recherché sinon
	 */
	public Optional<Computer> findById(long id) {
		return computerSearcher.fetchById(id);
	}

	/**
	 * Recherche de la liste de tous les ordinateurs présent dans la base de données
	 * 
	 * @return La liste des ordinateurs présents dans la base de données
	 */
	public List<Computer> fetchList() {
		return computerSearcher.fetchList();
	}

	public List<Computer> findListWithOffset(Pagination page) {
		return computerSearcher.fetchWithOffset(page);
	}
}
