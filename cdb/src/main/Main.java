package main;

import ui.CLInterface;

public class Main {
	
	//TODO Gestion des exceptions principalement dans la couche service, définitions d'exceptions propres 
	//TODO faire un vrai Singleton pour la classe DBConnection
	//TODO optionnel : Faire des DTO pour le modèle
	//TODO optionnel: Créer des interfaces associées à la déstatication de l'interface
	//TODO RELECTURE ET CORRECTION DES NOMS DE MERDE QUE J'AI MIS
	public static void main(String... strings) {
		CLInterface.start();
	}
}
