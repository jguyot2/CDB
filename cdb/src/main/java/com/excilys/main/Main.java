package com.excilys.main;

import com.excilys.ui.CLInterface;

public final class Main {
    // TODO Documentation des pages JSP
    // TODO les servlets
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
        CLInterface.start();
    }

    private Main() {
    }
}
