package ui;

import java.util.List;
import java.util.Scanner;

import model.Company;
import service.Pageable;

public class CompanyPaginationMenu {
	private int offset;
	private final static int nbCompanysPerPage = 30;
	private final static Scanner sc = new Scanner(System.in);

	private CompanyPaginationMenu() {
		offset = 0;
	}

	private void printNextPage() {
		List<Company> companys = Pageable.getCompanyPage(offset, nbCompanysPerPage);
		for (Company c : companys)
			System.out.println(c);
		this.offset += nbCompanysPerPage;
	}

	private void printPreviousPage() {
		this.offset -= nbCompanysPerPage;
		this.offset = this.offset < 0 ? 0 : this.offset;

		List<Company> companys = Pageable.getCompanyPage(offset, nbCompanysPerPage);
		for (Company c : companys)
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
		CompanyPaginationMenu page = new CompanyPaginationMenu();
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
