package com.excilys.model;

/**
 * Classe permettant la pagination. Garde l'identifiant
 *
 * @author jguyot2
 */
public class Page {
    /**
     * Nombre d'éléments par page par défaut.
     */
    private static final int BASE_NUMBER_OF_ELEMENTS_PER_PAGE = 30;
    /**
     * Nombre d'éléments à afficher.
     */
    private int totalNumberOfElements;
    /**
     * Nombre d'éléments pour une page.
     */
    private int pageLength = BASE_NUMBER_OF_ELEMENTS_PER_PAGE;
    /**
     * Nombre de pages.
     */
    private int nbOfPages;

    /**
     * Numéro de page courant.
     */
    private int pageNumber;

    /**
     * Création d'une page sur un ensemble contenant <limit>
     * éléments.
     *
     * @param numberOfElements
     */
    public Page(final int numberOfElements) {
        this.totalNumberOfElements = numberOfElements;
        this.nbOfPages = numberOfElements / pageLength
                + (numberOfElements % pageLength == 0 ? 0 : 1);
        pageNumber = 0;
    }

    /**
     * @return le numéro de la page courante.
     */
    public int getPageNumber() {
        return this.pageNumber;
    }

    /**
     * @return le nombre total de pages
     */
    public int getNbOfPAges() {
        return this.nbOfPages;
    }

    /**
     * @return le nombre d'éléments par page.
     */
    public int getPageLength() {
        return pageLength;
    }

    /**
     * @return le nombre total d'éléments qu'il est
     * possible d'afficher
     */
    public int getLimit() {
        return totalNumberOfElements;
    }

    /**
     * @return le nombre d'éléments avant le premier élément
     * de la page courante.
     */
    public int getOffset() {
        return this.pageNumber * this.pageLength;
    }

    /**
     * Avance d'une page.
     */
    public void goToNextPage() {
        if (this.pageNumber < this.nbOfPages) {
            this.pageNumber++;
        }
    }

    /**
     * Recule d'une page.
     */
    public void goToPreviousPage() {
        if (this.pageNumber > 0) {
            pageNumber--;
        }
    }

    /**
     * @param newElementsPerPage
     */
    public void setPageLength(final int newElementsPerPage) {
        this.pageLength = newElementsPerPage;
        this.nbOfPages = totalNumberOfElements / pageLength
                + (totalNumberOfElements % pageLength == 0 ? 0 : 1);
        this.pageNumber = nbOfPages < pageNumber ? nbOfPages - 1 : pageNumber;
    }

    /**
     * @param newLimit le nombre total d'éléments à changer.
     */
    public void setLimit(final int newLimit) {
        this.totalNumberOfElements = newLimit;
    }
}
