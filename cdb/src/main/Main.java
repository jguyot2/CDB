package main;

import ui.CLInterface;

public class Main {
	//TODO Dé-staticiser le service et la persistance
	//TODO optionnel: Créer des interfaces associées
	//TODO Récupération de la taille de la table lors de la pagination
	//TODO Utilisation de LocalDate dans la classe Computer
	//TODO Gestion des exceptions principalement dans la couche service, définitions d'exceptions propres 
	//TODO Mettre des listes pour la gestion des erreurs (et dire ce qui n'a pas été fait) 
	//TODO faire un vrai Singleton pour la classe DBConnection
	//TODO optionnel : Faire des DTO pour le modèle
	//TODO RELECTURE ET CORRECTION DES NOMS DE MERDE QUE J'AI MIS
	public static void main(String... strings) {
		CLInterface.start();
	}
}
