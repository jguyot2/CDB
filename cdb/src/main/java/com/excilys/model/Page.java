package com.excilys.model;

/**
 * Classe permettant la pagination. Garde l'identifiant
 *
 * @author jguyot2
 */
public final class Page {
    /**
     * Nombre d'éléments par page par défaut.
     */
    private static final int BASE_NUMBER_OF_ELEMENTS_PER_PAGE = 10;

    /** */
    private int pageLength = BASE_NUMBER_OF_ELEMENTS_PER_PAGE;

    /** commence à 0. */
    private int pageNumber;

    /** */
    private int totalNumberOfElements;

    /** */
    public Page() {
    }

    /**
     * Création d'une page sur un ensemble contenant <limit> éléments.
     *
     * @param numberOfElements
     */
    public Page(final int numberOfElements) {
        this.totalNumberOfElements = numberOfElements;

        this.pageNumber = 0;
    }

    /** */
    public Page(final int numberOfElements, final int pageNumber, final int elementsPerPage) {
        this.totalNumberOfElements = numberOfElements;
        this.pageNumber = pageNumber;
        this.pageLength = elementsPerPage;
    }

    /** */
    public int getLimit() {
        return this.totalNumberOfElements;
    }

    public int getNbOfPages() {
        return this.totalNumberOfElements / this.pageLength
                + (this.totalNumberOfElements % this.pageLength == 0 ? 0 : 1);
    }

    /**
     * @return le nombre d'éléments avant le premier élément de la page courante.
     */
    public int getOffset() {
        return this.pageNumber * this.pageLength;
    }

    /** */
    public int getPageLength() {
        return this.pageLength;
    }

    /** */
    public int getPageNumber() {
        return this.pageNumber;
    }

    public int getTotalNumberOfElements() {
        return this.totalNumberOfElements;
    }

    /** */
    public void goToNextPage() {
        if (this.pageNumber < getNbOfPages()) {
            this.pageNumber++;
        }
    }

    /** */
    public void goToPreviousPage() {
        if (this.pageNumber > 0) {
            this.pageNumber--;
        }
    }

    /**
     * @param newLimit le nombre total d'éléments à changer.
     */
    public void setLimit(final int newLimit) {
        this.totalNumberOfElements = newLimit;
    }

    /**
     * @param newElementsPerPage
     */
    public void setPageLength(final int newElementsPerPage) {
        this.pageLength = newElementsPerPage;
        this.pageNumber = getNbOfPages() < this.pageNumber ? getNbOfPages() - 1 : this.pageNumber;
    }

    public void setPageNumber(final int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setTotalNumberOfElements(final int totalNumberOfElements) {
        this.totalNumberOfElements = totalNumberOfElements;

    }

}
