package com.excilys.main;

public final class Main {
    // TODO Documentation des pages JSP
    // TODO les servlets
    //  -> Servlet modification d'ordi
    //  -> Servlet création d'ordi
    //  -> Servlet suppression d'ordi
    //  -> Servlet lecture paginée
    // TODO automatisation avec Selenium ?
    // TODO Tests JUnit (prévoir la plus grosse couverture possible)
    // TODO Création d'une classe ComputerDTOValidator séparée de
    // ComputerValidator, même chose pour Company
    
    /**
     * Classe principale.
     *
     * @param strings paramètres du programme
     */
    public static void main(final String... strings) {
        com.excilys.ui.CLInterface.start();
    }

    private Main() {
    }
}
