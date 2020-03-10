package ui;
/**
 * Enumération énumérant les commandes disponibles pour l'interface en ligne de commande
 * @author jguyot2
 *
 */
public enum Commandes {
	LIST_COMPUTERS, 
	LIST_COMPANIES,
	SHOW_DETAILS, 
	CREATE_COMPUTER, 
	UPDATE_COMPUTER, 
	DELETE_COMPUTER,
	EXIT;
	
	public static Commandes getCommandeFromInput(int input) {
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
		default:
			throw new IllegalArgumentException();
		}
	}
}
