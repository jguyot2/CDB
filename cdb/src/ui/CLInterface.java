package ui;

/**
 * Interface utilisateur permettant l'interaction par ligne de commande
 * @author jguyot2
 *
 */
public class CLInterface {
	public static void getCommande() {
		System.out.println("Entrez la commande: ");
		System.out.println("1:\t Lister les entreprises");
		System.out.println("2 <ID> :\t Détailler un ordinateur");
		System.out.println("3 <ID> :\t Création d'un ordinateur");
		System.out.println("4 <ID> :\t Mise à jour d'un ordinateur");
		System.out.println("5 <ID> :\t Suppression d'un ordinateur");
		
	}
}
