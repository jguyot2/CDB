package com.excilys.ui;

import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.model.Computer;
import com.excilys.model.Page;
import com.excilys.service.ComputerService;
import com.excilys.ui.config.UiConfig;

@Component
public class ComputerPagination {

    private ComputerService computerValidator;
    private final static Scanner sc = new Scanner(System.in);

    public static void paginate() {
        // TODO : réinitialisation
        ComputerPagination page = UiConfig.getContext().getBean(ComputerPagination.class);
        boolean exit = false;
        while (!exit) {
            PaginationCommand userCommand = page.getCommand();
            exit = page.executeCommand(userCommand);
        }
    }

    private Page page;

    @Autowired
    private ComputerPagination(ComputerService service) {
        this.computerValidator = service;
        this.page = new Page(this.computerValidator.getNumberOfElements());
    }

    public boolean executeCommand(final PaginationCommand command) {
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

    PaginationCommand getCommand() {
        printMenu();
        String userEntry = sc.nextLine().trim();
        if ("n".equals(userEntry)) {
            return PaginationCommand.NEXT;
        } else if ("p".equals(userEntry)) {
            return PaginationCommand.PREVIOUS;
        } else if ("e".equals(userEntry)) {
            return PaginationCommand.EXIT;
        } else if ("c".equals(userEntry)) {
            return PaginationCommand.CURRENT;
        }
        System.out.println("Commande invalide.");
        return getCommand();
    }

    private void printCurrentPage() {
        List<Computer> computers = this.computerValidator.fetchList(this.page);
        for (Computer c : computers) {
            System.out.println(c);
        }
    }

    private void printMenu() {
        System.out.println("---------------------------------");
        System.out.println("n: afficher la prochaine page");
        System.out.println("p: afficher la page précédente");
        System.out.println("e: Sortie de la pagination");
        System.out.println("c: affichage de la page courante");
        System.out.println("---------------------------------");
    }

    private void printNextPage() {
        this.page.goToNextPage();
        List<Computer> computers = this.computerValidator.fetchList(this.page);
        for (Computer c : computers) {
            System.out.println(c.getShortDescription());
        }
    }

    private void printPreviousPage() {
        this.page.goToPreviousPage();
        List<Computer> computers = this.computerValidator.fetchList(this.page);
        for (Computer c : computers) {
            System.out.println(c);
        }
    }
}
