package ui;

import java.util.List;
import java.util.Scanner;

import model.Computer;
import service.Pageable;

public class ComputerPagination {
	
	private int offset;
	private final static int nbComputersPerPage = 20;
	private final static Scanner sc = new Scanner(System.in);

	private ComputerPagination() {
		offset = 0;
	}

	private void printNextPage() {
		this.offset += nbComputersPerPage;
		List<Computer> computers = Pageable.getComputerPage(offset, nbComputersPerPage);
		for (Computer c : computers)
			System.out.println(c.getShortDescription());
	}
	
	private void printCurrentPage() {
		List<Computer> computers = Pageable.getComputerPage(offset, nbComputersPerPage);
		for (Computer c : computers)
			System.out.println(c);		
	}
	
	private void printPreviousPage() {
		this.offset -= nbComputersPerPage;
		this.offset = this.offset < 0 ? 0 : this.offset;

		List<Computer> computers = Pageable.getComputerPage(offset, nbComputersPerPage);
		for (Computer c : computers)
			System.out.println(c);
	}

	private void printMenu() {
		System.out.println("---------------------------------");
		System.out.println("n: afficher la prochaine page");
		System.out.println("p: afficher la page précédente");
		System.out.println("e: Sortie de la pagination");
		System.out.println("c: affichage de la page courante");
		System.out.println("---------------------------------");
	}

	PaginationCommand getCommand() {
		printMenu();
		String userEntry = sc.nextLine().trim();
		if ("n".equals(userEntry))
			return PaginationCommand.NEXT;
		else if ("p".equals(userEntry))
			return PaginationCommand.PREVIOUS;
		else if ("e".equals(userEntry))
			return PaginationCommand.EXIT;
		else if("c".equals(userEntry))
			return PaginationCommand.CURRENT;
		System.out.println("Commande invalide.");
		return getCommand();
	}

	public static void paginate() {
		ComputerPagination page = new ComputerPagination();
		boolean exit = false;
		while (!exit) {
			PaginationCommand userCommand = page.getCommand();
			exit = page.executeCommand(userCommand);
		}
	}

	public boolean executeCommand(PaginationCommand command) {
		switch (command) {
		case EXIT:
			return true;
		case NEXT:
			printNextPage();
			return false;
		case PREVIOUS:
			printPreviousPage();
			return false;
		case CURRENT:
			printCurrentPage();
			return false;
		}
		return false;
	}
}
