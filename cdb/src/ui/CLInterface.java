package ui;

import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import mapper.DateMapper;
import model.Company;
import model.Computer;
import service.CompanyValidator;
import service.ComputerValidator;

/**
 * Interface utilisateur permettant l'interaction avec la BD par ligne de
 * commande
 * 
 * @author jguyot2
 *
 */
public class CLInterface {

	private static Scanner sc = new Scanner(System.in);

	/**
	 * Affiche une courte description des ordinateurs dans la base de données
	 */
	private static void listComputersCommand() {
		List<Computer> computerList = ComputerValidator.fetchComputerList();
		for (Computer c : computerList)
			System.out.println(c.getShortDescription());
		System.out.println("-------------------------");
	}

	/**
	 * Affiche la liste des entreprises de la base de données
	 */
	private static void listCompaniesCommand() {
		List<Company> companyList = CompanyValidator.findCompaniesList();
		for (Company c : companyList)
			System.out.println(c);
		System.out.println("-------------------------");
	}

	/**
	 * Affiche une description détaillée d'une instance de Computer d'id entré par
	 * l'utilisateur
	 */
	private static void getComputerDetailsCommand() {
		System.out.println("Entrez l'identifiant de l'ordinateur recherché");
		String strComputerId = sc.nextLine();
		long computerId = Long.parseLong(strComputerId);
		Optional<Computer> computerOpt = ComputerValidator.fetchComputerById(computerId);
		if (!computerOpt.isPresent()) {
			System.out.println("L'ordinateur n'a pas été trouvé dans la BD");
		} else {
			Computer computer = computerOpt.get();
			System.out.println(computer);
		}
		System.out.println("-------------------------");
	}

	/**
	 * Création d'un Computer par l'utilisateur, puis ajout dans la base de données
	 * si l'utilisateur a pas entré n'importe quoi
	 * 
	 * TODO: refactoriser la fonction parce qu'elle est beaucoup trop longue
	 */
	private static void createComputerCommand() {
		System.out.println("Entrez le nom de l'ordinateur");
		String computerName = sc.nextLine().trim();
		System.out.println("Nom entré:" + computerName);

		System.out.println("Entrez l'identifiant de la compagnie associée");
		String strCompanyId = sc.nextLine().trim();
		System.out.println("ID entré:" + strCompanyId);

		Company company = null;
		if (!strCompanyId.isEmpty()) {
			long companyId = Long.parseLong(strCompanyId);
			Optional<Company> companyOpt = CompanyValidator.findCompanyById(companyId);
			if (companyOpt.isPresent())
				company = companyOpt.get();
		}
		System.out.println("Company:" + company);
		System.out.println("Entrez la date d'introduction au format DD/MM/YYYY");
		String dateStr = sc.nextLine().trim();
		System.out.println("STR INTRO:" + dateStr);

		Date introduced = null;
		if (!dateStr.isEmpty())
			introduced = DateMapper.stringToUtilDate(dateStr);
		System.out.println("Date:" + introduced);
		System.out.println("Entrez la date d'arrêt de production au format DD/MM/YYYY");
		String strDiscontinuation = sc.nextLine().trim();
		Date discontinued = null;
		if (!strDiscontinuation.isEmpty())
			discontinued = DateMapper.stringToUtilDate(strDiscontinuation);

		Computer createdComputer = new Computer(computerName, company, introduced, discontinued);
		long newIdComputer = ComputerValidator.createComputer(createdComputer);
		if (newIdComputer == 0)
			System.out.println("L'ordinateur n'a pas été enregistré");
		else
			System.out.println("L'ordinateur a été créé, et est d'id :" + newIdComputer);
	}

	/**
	 * Suppression d'un ordinateur d'id saisi par l'utilisateur
	 */
	private static void deleteComputerCommand() {
		System.out.println("Saisissez l'identifiant du pc à supprimer");
		String idString = sc.nextLine().trim();
		long id = Long.parseLong(idString, 0);
		if (id == 0) {
			System.out.println("Identifiant invalide.");
			return;
		}
		if (ComputerValidator.deleteComputer(id) > 0)
			System.out.println("La ligne devrait être supprimée");
		else
			System.out.println("Le computer a pas été supprimé");
	}

	private static void updateComputerCommand() {
		System.out.println("Entrez l'id de l'ordinateur à modifier");
		String idString = sc.nextLine().trim();
		long id = Long.parseLong(idString);

		Optional<Computer> optFoundComputer = ComputerValidator.fetchComputerById(id);
		if (!optFoundComputer.isPresent()) {
			System.out.println("Ordinateur non trouvé.");
			return;
		}
		Computer foundComputer = optFoundComputer.get();

		System.out.println("Modification du nom: Ne rien entrer pour ne pas changer, entrer un nom sinon");
		System.out.println("Nom courant: " + foundComputer.getName());
		String newName = sc.nextLine().trim();
		if (!newName.isEmpty())
			foundComputer.setName(newName);
		System.out.println("Modification de l'identifiant de l'entreprise: "
				+ "Ne rien entrer pour ne pas changer, entrer 'NONE' pour supprimer l'entreprise");

		System.out.println("Entreprise courante:" + foundComputer.getManufacturer());
		String newEnterpriseIdStr = sc.nextLine().trim();
		if (!newEnterpriseIdStr.isEmpty())
			if (newEnterpriseIdStr.equals("NONE"))
				foundComputer.setId(0);
			else
				foundComputer.setId(Long.parseLong(newEnterpriseIdStr));

		System.out.println("Entrez la date d'introduction sous forme `JJ/MM/AAAA`: "
				+ "Ne rien entrer pour laisser la date d'intro telle quelle, entrer `NONE` "
				+ "pour supprimer cette date");
		System.out.println("Date d'intro courante: " + foundComputer.getIntroduction());

		String strIntro = sc.nextLine().trim();
		if (!strIntro.isEmpty())
			if ("NONE".equals(strIntro))
				foundComputer.setIntroduction(null);
			else
				foundComputer.setIntroduction(mapper.DateMapper.stringToUtilDate(strIntro));
		System.out.println("Entrez la date de discontinuation sous forme `JJ/MM/AAAA`: "
				+ "Ne rien entrer pour laisser la date d'intro telle quelle, entrer `NONE` "
				+ "pour supprimer cette date");
		System.out.println("Date d'intro courante: " + foundComputer.getDiscontinuation());

		String strDiscontinuation = sc.nextLine().trim();
		if (!strDiscontinuation.isEmpty())
			if ("NONE".equals(strDiscontinuation))
				foundComputer.setDiscontinuation(null);
			else
				foundComputer.setDiscontinuation(mapper.DateMapper.stringToUtilDate(strDiscontinuation));
		
		ComputerValidator.updateComputer(foundComputer);
	}

	private static void exitCommand() {
		System.out.println("Sortie du programme");
		System.exit(0);
	}

	private static void printMenu() {
		System.out.println("Entrez la commande: ");
		System.out.println("0:\t Lister les ordinateurs");
		System.out.println("1:\t Lister les entreprises");
		System.out.println("2:\t Détails d'un ordinateur");
		System.out.println("3:\t Création d'un ordinateur");
		System.out.println("4:\t Mise à jour d'un ordinateur");
		System.out.println("5:\t Suppression d'un ordinateur");
		System.out.println("6:\t Sortie du programme");
		System.out.println("--------------------------------");
	}

	/**
	 * Exécution d'une commande donnée en paramètre
	 * 
	 * @param commandToExecute La commande à exécuter
	 */
	private static void executeCommand(Commandes commandToExecute) {
		switch (commandToExecute) {
		case LIST_COMPUTERS:
			listComputersCommand();
			break;
		case LIST_COMPANIES:
			listCompaniesCommand();
			break;
		case SHOW_DETAILS:
			getComputerDetailsCommand();
			break;
		case CREATE_COMPUTER:
			createComputerCommand();
			break;
		case UPDATE_COMPUTER:
			updateComputerCommand();
			break;
		case DELETE_COMPUTER:
			deleteComputerCommand();
			break;
		case EXIT:
			exitCommand();
			break;
		default:
			throw new RuntimeException("arrivée dans le default alors que c'est pas censé arriver");
		}
	}

	/**
	 * affichage d'une erreur+le message la décrivant
	 * 
	 * @param e l'exception ayant provoqué l'erreur
	 */
	private static void printErrorMessage(Exception e) {
		System.out.println("Entrée invalide; " + e.getMessage());
	}

	/**
	 * Fonction affichant un menu et exécutant une commande rentrée par
	 * l'utilisateur
	 *
	 *
	 * TODO : Rattraper les exception ailleurs parce que tout rattraper ici c'est
	 * assez sale
	 */
	public static void getCommande() {
		printMenu();
		try {
			String strCommandId = sc.nextLine();
			int commandId = Integer.parseInt(strCommandId);
			Commandes commandToExecute = Commandes.getCommandeFromInput(commandId);

			executeCommand(commandToExecute);
		} catch (InputMismatchException e) {
			printErrorMessage(e);
		} catch (IllegalArgumentException e) {
			printErrorMessage(e);
		}
	}
}
