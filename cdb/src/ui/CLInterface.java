package ui;

import java.util.Date;

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
 */
public class CLInterface {

	private static Scanner sc = new Scanner(System.in);

	private static void listComputersCommand() {
		List<Computer> computerList = ComputerValidator.fetchComputerList();
		for (Computer c : computerList)
			System.out.println(c.getShortDescription());
		System.out.println("-------------------------");
	}

	private static void listCompaniesCommand() {
		List<Company> companyList = CompanyValidator.fetchList();
		for (Company c : companyList)
			System.out.println(c);
		System.out.println("-------------------------");
	}

	private static void getComputerDetailsCommand() {
		System.out.println("Entrez l'identifiant de l'ordinateur recherché");
		String strComputerId = sc.nextLine();
		long computerId = 0;
		try {
			computerId = Long.parseLong(strComputerId, 10);
		} catch (NumberFormatException e) {
			System.out.println("Ce qui a été entré ne correspond pas à un identifiant valide");
			return;
		}

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
	 */
	private static void createComputerCommand() {
		System.out.println("Entrez le nom de l'ordinateur");
		String computerName = sc.nextLine().trim();
		System.out.println("Nom entré:'" + computerName + "'");

		System.out.println("Entrez l'identifiant de la compagnie associée (ou une ligne vide pour ne rien ajouter)");
		String strCompanyId = sc.nextLine().trim();
		System.out.println("ID entré:" + strCompanyId);

		Company company = null;

		if (!strCompanyId.isEmpty()) {
			long companyId = 0;
			try {
				companyId = Long.parseLong(strCompanyId);
			} catch (NumberFormatException e) {
				System.out.println("L'identifiant entré ne correspond pas à un ID. Fin de la saisie");
				return;
			}
			Optional<Company> companyOpt = CompanyValidator.findCompanyById(companyId);
			if (companyOpt.isPresent())
				company = companyOpt.get();
			else
				System.out.println("Entreprise non trouvée dans la bd");
		}
		System.out.println("Company:" + company);
		System.out.println("Entrez la date d'introduction au format DD/MM/YYYY");
		String dateStr = sc.nextLine().trim();
		System.out.println("STR INTRO:" + dateStr);
		Date introduced = null;
		if (!dateStr.isEmpty())
			try {
				introduced = DateMapper.stringToUtilDate(dateStr);
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
				System.out.println("Fin de la saisie, car date no parsable");
				return;
			}
		System.out.println("Date:" + introduced);
		System.out.println("Entrez la date d'arrêt de production au format DD/MM/YYYY");
		String strDiscontinuation = sc.nextLine().trim();
		Date discontinued = null;
		if (!strDiscontinuation.isEmpty())
			try {
				discontinued = DateMapper.stringToUtilDate(strDiscontinuation);
			} catch (IllegalArgumentException e) {
				System.out.println("Erreur : " + e.getMessage());
				System.out.println("Date saisie invalide, fin de l'entrée");
				return;
			}

		Computer createdComputer = new Computer(computerName, company, introduced, discontinued);
		long newIdComputer = ComputerValidator.createComputer(createdComputer);
		if (newIdComputer == 0)
			System.out.println("L'ordinateur n'a pas été enregistré");
		else
			System.out.println("L'ordinateur a été créé, et est d'id :" + newIdComputer);
	}

	private static void deleteComputerCommand() {

		System.out.println("Saisissez l'identifiant du pc à supprimer");
		String idString = sc.nextLine().trim();
		long id = 0;
		try {
			id = Long.parseLong(idString, 0);
		} catch (NumberFormatException e) {
			System.out.println("Identifiant invalide");
			return;
		}

		if (ComputerValidator.deleteComputer(id) > 0)
			System.out.println("le PC a été supprimé");
		else
			System.out.println("L'ordinateur de n'a pas été supprimé dans la BD");

	}

	private static void updateComputerCommand() {
		System.out.println("Entrez l'id de l'ordinateur à modifier");
		String idString = sc.nextLine().trim();
		long id = 0;
		try {
			id = Long.parseLong(idString);
		} catch (NumberFormatException e) {
			System.out.println("La valeur entrée ne correspond pas à un ID. ");
			return;
		}

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
			else {
				long idComputer = 0;
				try {
					idComputer = Long.parseLong(newEnterpriseIdStr);
				} catch (NumberFormatException e) {
					System.out.println("Id invalide entré, fin de la saisie");
					return;
				}
				foundComputer.setId(idComputer);
			}
		System.out.println("Entrez la date d'introduction sous forme `JJ/MM/AAAA`: "
				+ "Ne rien entrer pour laisser la date d'intro telle quelle, entrer `NONE` "
				+ "pour supprimer cette date");
		System.out.println("Date d'intro courante: " + foundComputer.getIntroduction());

		String strIntro = sc.nextLine().trim();
		if (!strIntro.isEmpty())
			if ("NONE".equals(strIntro))
				foundComputer.setIntroduction(null);
			else
				try {
					foundComputer.setIntroduction(mapper.DateMapper.stringToUtilDate(strIntro));
				} catch (IllegalArgumentException e) {
					System.out.println("Erreur :" + e.getMessage());
					return;
				}

		System.out.println("Entrez la date de discontinuation sous forme `JJ/MM/AAAA`: "
				+ "Ne rien entrer pour laisser la date d'intro telle quelle, entrer `NONE` "
				+ "pour supprimer cette date");
		System.out.println("Date d'intro courante: " + foundComputer.getDiscontinuation());

		String strDiscontinuation = sc.nextLine().trim();
		if (!strDiscontinuation.isEmpty())
			if ("NONE".equals(strDiscontinuation))
				foundComputer.setDiscontinuation(null);
			else
				try {
					foundComputer.setDiscontinuation(mapper.DateMapper.stringToUtilDate(strDiscontinuation));
				} catch (IllegalArgumentException e) {
					System.out.println("Erreur :" + e.getMessage());
					return;
				}
		int updated = ComputerValidator.updateComputer(foundComputer);
		if (updated > 0) {
			System.out.println("La mise a jour a été effectuée");
		} else
			System.out.println("La mise à jour n'a pas eu lieu");
	}

	private static void exitCommand() {
		System.out.println("Sortie du programme");
		System.exit(0);
	}

	private static void printMenu() {
		System.out.println("--------------------------------");
		System.out.println("Entrez la commande: ");
		System.out.println("0:\t Lister les ordinateurs");
		System.out.println("1:\t Lister les entreprises");
		System.out.println("2:\t Détails d'un ordinateur");
		System.out.println("3:\t Création d'un ordinateur");
		System.out.println("4:\t Mise à jour d'un ordinateur");
		System.out.println("5:\t Suppression d'un ordinateur");
		System.out.println("6:\t Sortie du programme");
		System.out.println("7:\t Pagination des ordinateurs");
		System.out.println("8:\t Pagination des entreprises");
		System.out.println("--------------------------------");
	}

	/**
	 * Exécution d'une commande donnée en paramètre
	 * 
	 * @param commandToExecute La commande à exécuter
	 */
	private static void executeCommand(Commands commandToExecute) {
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
		case COMPUTER_PAGINATION:
			ComputerPaginationMenu.paginate();
			break;
		case COMPANY_PAGINATION:
			CompanyPaginationMenu.paginate();
			break;
		default:
			throw new RuntimeException("arrivée dans le default alors que c'est pas censé arriver");
		}
	}

	/**
	 * Fonction affichant un menu et exécutant une commande rentrée par
	 * l'utilisateur
	 */
	public static void getCommande() {
		printMenu();
		String strCommandId = sc.nextLine();
		int commandId = -1;
		try {
			commandId = Integer.parseInt(strCommandId);

		} catch (NumberFormatException e) {
			System.out.println("La commande rentrée est invalide");
		}
		
		try {
			Commands commandToExecute = Commands.getCommandeFromInput(commandId);
			executeCommand(commandToExecute);
		} catch (IllegalArgumentException e) {
			System.out.println("Erreur" + e.getMessage());
		}
	}
}
