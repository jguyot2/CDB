package com.excilys.main;

public final class Main {
    // TODO : «harmonisation» des noms de méthodes équivalentes dans les enums
    // computerinstanceproblems/dtocomputerinstanceproblems
    // TODO Documentation des pages JSP
    // TODO les servlets
    // -> Servlet modification d'ordi
    // -> Servlet création d'ordi
    // -> Servlet suppression d'ordi
    // TODO automatisation avec Selenium ?
    // TODO Tests JUnit (prévoir la plus grosse couverture possible)

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
