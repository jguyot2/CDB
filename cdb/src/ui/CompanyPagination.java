package ui;

import java.util.List;
import java.util.Scanner;

import model.Company;
import model.Pagination;
import service.CompanyValidator;

public class CompanyPagination {
	private Pagination page;
	private final static Scanner sc = new Scanner(System.in);
	private static CompanyValidator companyPagination = new CompanyValidator();
	
	private CompanyPagination() {
		this.page = new Pagination();
	}

	private void printNextPage() {
		List<Company> companys = companyPagination.fetchWithOffset(page);
		for (Company c : companys)
			System.out.println(c);
		this.page.goToNextPage();
	}

	private void printCurrentPage() {
		List<Company> companys = companyPagination.fetchWithOffset(page);
			for (Company c : companys)
			System.out.println(c);
	}

	private void printPreviousPage() {
		this.page.goToPreviousPage();

		List<Company> companys = companyPagination.fetchWithOffset(page);
		for (Company c : companys)
			System.out.println(c);
	}

	private void printMenu() {
		System.out.println("--------------------------------");
		System.out.println("n: afficher la prochaine page");
		System.out.println("p: afficher la page précédente");
		System.out.println("e: Sortie de la pagination");
		System.out.println("c: Afficher la page courante");
		System.out.println("--------------------------------");
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
		CompanyPagination page = new CompanyPagination();
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
