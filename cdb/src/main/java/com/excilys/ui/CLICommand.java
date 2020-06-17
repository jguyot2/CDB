package com.excilys.ui;

/**
 * Enumération énumérant les commandes disponibles pour l'interface en ligne de
 * commande
 *
 * @author jguyot2
 *
 */
public enum CLICommand {
    COMPANY_PAGINATION, COMPUTER_PAGINATION, CREATE_COMPUTER, DELETE_COMPUTER, EXIT, LIST_COMPANIES,
    LIST_COMPUTERS, SHOW_DETAILS, UPDATE_COMPUTER, COMPANY_DELETE;

    /**
     * Fonction associant un entier en paramètre à la commande à exécuter
     *
     * @param input l'entier rentré par l'utilisateur
     * @return la commande à exécuter
     */
    public static CLICommand getCommandeFromInput(final int input) {
        switch (input) {
            case 0:
                return LIST_COMPUTERS;
            case 1:
                return LIST_COMPANIES;
            case 2:
                return SHOW_DETAILS;
            case 3:
                return CREATE_COMPUTER;
            case 4:
                return UPDATE_COMPUTER;
            case 5:
                return DELETE_COMPUTER;
            case 6:
                return EXIT;
            case 7:
                return COMPUTER_PAGINATION;
            case 8:
                return COMPANY_PAGINATION;
            case 9:
                return COMPANY_DELETE;
            default:
                throw new IllegalArgumentException("Entier ne correspondant pas à une commande !");
        }
    }
}
