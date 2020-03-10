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
 * Interface utilisateur permettant l'interaction par ligne de commande
 * 
 * @author jguyot2
 *
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
		List<Company> companyList = CompanyValidator.findCompaniesList();
		for (Company c : companyList)
			System.out.println(c);
		System.out.println("-------------------------");
	}

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

	private static void deleteComputerCommand() {
		System.out.println("Saisissez l'identifiant du pc à supprimer");
		String idString = sc.nextLine().trim();
		long id = Long.parseLong(idString, 0);
		if (id == 0) {
			System.out.println("Identifiant invalide.");
			return;
		}
		
		if (ComputerValidator.deleteComputer(id))
			System.out.println("La ligne devrait être supprimée");
		else
			System.out.println("Le computer a pas forcément été supprimé");
		
	}

	private static void updateComputerCommand() {
		
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

	private static void printErrorMessage(Exception e) {
		System.out.println("Entrée invalide; " + e.getMessage());
	}

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
