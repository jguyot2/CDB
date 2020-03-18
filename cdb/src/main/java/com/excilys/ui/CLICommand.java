package com.excilys.ui;
/**
 * Enumération énumérant les commandes disponibles pour l'interface en ligne de commande
 * @author jguyot2
 *
 */
public enum CLICommand {
	LIST_COMPUTERS, 
	LIST_COMPANIES,
	SHOW_DETAILS, 
	CREATE_COMPUTER, 
	UPDATE_COMPUTER, 
	DELETE_COMPUTER,
	EXIT,
	COMPUTER_PAGINATION,
	COMPANY_PAGINATION;
	
	/**
	 * Fonction associant un entier en paramètre à la commande à exécuter
	 * @param input l'entier rentré par l'utilisateur 
	 * @return la commande à exécuter
	 */
	public static CLICommand getCommandeFromInput(int input) {
		switch(input) {
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
		default:
			throw new IllegalArgumentException("Entier ne correspondant pas à une commande !");
		}
	}
}