package ui;

import java.util.List;
import java.util.Scanner;

import model.Company;
import service.Pageable;

public class CompanyPagination {
	private int offset;
	private final static int nbCompanysPerPage = 20;
	private final static Scanner sc = new Scanner(System.in);

	private CompanyPagination() {
		offset = 0;
	}

	private void printNextPage() {
		List<Company> companys = Pageable.getCompanyPage(offset, nbCompanysPerPage);
		for (Company c : companys)
			System.out.println(c);
		this.offset += nbCompanysPerPage;
	}

	private void printCurrentPage() {
		List<Company> companys = Pageable.getCompanyPage(offset, nbCompanysPerPage);
		for (Company c : companys)
			System.out.println(c);
	}

	private void printPreviousPage() {
		this.offset -= nbCompanysPerPage;
		this.offset = this.offset < 0 ? 0 : this.offset;

		List<Company> companys = Pageable.getCompanyPage(offset, nbCompanysPerPage);
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
