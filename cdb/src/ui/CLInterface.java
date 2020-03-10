package ui;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Interface utilisateur permettant l'interaction par ligne de commande
 * @author jguyot2
 *
 */
public class CLInterface {
	private static Scanner sc = new Scanner(System.in);
	
	private static void listComputersCommand() {
		
	}
	private static void listCompaniesCommand() {
		
	}
	private static void getComputerDetailsCommand() {
		
	}
	private static void createComputerCommand() {
		
	}
	private static void deleteComputerCommand() {
		
	}
	
	private static void updateComputerCommand() {
		
	}
	
	private static void exitCommand() {
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
	}
	
	private static void executeCommand(Commandes commandToExecute) {
		switch(commandToExecute) {
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
	private static void printErrorMessage() {
		System.out.println("Entrée invalide");
	}
	
	public static void getCommande() {
		printMenu();
		try {
			int commandId = sc.nextInt(); 
			Commandes commandToExecute= Commandes.getCommandeFromInput(commandId);
			executeCommand(commandToExecute);
		} catch ( InputMismatchException e ) {
			printErrorMessage();
		} catch ( IllegalArgumentException e ) {
			printErrorMessage();
		}
	}
}
