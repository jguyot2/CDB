package ui;

import java.util.List;
import java.util.Scanner;

import model.Computer;
import service.Pageable;

public class ComputerPaginationMenu {
	private int offset;
	private final static int nbComputersPerPage = 30;
	private final static Scanner sc = new Scanner(System.in);

	private ComputerPaginationMenu() {
		offset = 0;
	}

	private void printNextPage() {
		List<Computer> computers = Pageable.getComputerPage(offset, nbComputersPerPage);
		for (Computer c : computers)
			System.out.println(c);
		this.offset += nbComputersPerPage;
	}

	private void printPreviousPage() {
		this.offset -= nbComputersPerPage;
		this.offset = this.offset < 0 ? 0 : this.offset;

		List<Computer> computers = Pageable.getComputerPage(offset, nbComputersPerPage);
		for (Computer c : computers)
			System.out.println(c);
	}

	private void printMenu() {
		System.out.println("n: afficher la prochaine page");
		System.out.println("p: afficher la page précédente");
		System.out.println("e: Sortie de la pagination");
	}

	PaginationCommands getCommand() {
		printMenu();
		String userEntry = sc.nextLine().trim();
		if ("n".equals(userEntry))
			return PaginationCommands.NEXT;
		else if ("p".equals(userEntry))
			return PaginationCommands.PREVIOUS;
		else if ("e".equals(userEntry))
			return PaginationCommands.EXIT;

		System.out.println("Commande invalide.");
		return getCommand();
	}

	public static void paginate() {
		ComputerPaginationMenu page = new ComputerPaginationMenu();
		boolean exit = false;
		while (!exit) {
			PaginationCommands userCommand = page.getCommand();
			exit = page.executeCommand(userCommand);
		}

	}

	public boolean executeCommand(PaginationCommands command) {
		switch (command) {
		case EXIT:
			return true;
		case NEXT:
			printNextPage();
			return false;
		case PREVIOUS:
			printPreviousPage();
			return false;
		}
		return false;
	}
}
