package com.excilys.main;

public final class Main {

    // TODO Tests JUnit ( prévoir la plus grosse couverture possible )
    // TODO A partir du Template, intégration des features à partir
    // de pages JSP et
    // servlets
    // TODO même chose mais avec JSTL
    // TODO automatisation avec Selenium ?

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
