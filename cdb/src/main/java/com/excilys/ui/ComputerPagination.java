package com.excilys.ui;

import java.util.List;
import java.util.Scanner;

import com.excilys.model.Computer;
import com.excilys.model.Page;
import com.excilys.service.ComputerValidator;

public class ComputerPagination {
	private Page page;
	private final static Scanner sc = new Scanner(System.in);
	private final static ComputerValidator computerValidator = new ComputerValidator();

	private ComputerPagination() {
		this.page = new Page(computerValidator.getNumberOfElements());
	}

	private void printNextPage() {
		this.page.goToNextPage();
		List<Computer> computers = computerValidator.fetchWithOffset(page);
		for (Computer c : computers)
			System.out.println(c.getShortDescription());
	}

	private void printCurrentPage() {
		List<Computer> computers = computerValidator.fetchWithOffset(page);
		for (Computer c : computers)
			System.out.println(c);
	}

	private void printPreviousPage() {
		this.page.goToPreviousPage();
		List<Computer> computers = computerValidator.fetchWithOffset(page);
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
		else if ("c".equals(userEntry))
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
