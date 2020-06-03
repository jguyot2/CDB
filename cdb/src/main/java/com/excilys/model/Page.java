package com.excilys.model;

import com.excilys.service.NotImplementedException;

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

    /** */
    private int nbOfPages;

    /** */
    private int pageLength = BASE_NUMBER_OF_ELEMENTS_PER_PAGE;

    /** commence à 0 */
    private int pageNumber;

    /** */
    private int totalNumberOfElements;

    public Page() {
    }

    /**
     * Création d'une page sur un ensemble contenant <limit>
     * éléments.
     *
     * @param numberOfElements
     */
    public Page(final int numberOfElements) {
        totalNumberOfElements = numberOfElements;
        nbOfPages = numberOfElements / pageLength
            + (numberOfElements % pageLength == 0 ? 0 : 1);
        pageNumber = 0;
    }

    public Page(final int numberOfElements, final int pageNumber, final int elementsPerPage) {
        throw new NotImplementedException();
    }

    /** */
    public int getLimit() {
        return totalNumberOfElements;
    }

    public int getNbOfPages() {
        return nbOfPages;
    }

    /** */
    public int getNbOfPAges() {
        return nbOfPages;
    }

    /**
     * @return le nombre d'éléments avant le premier élément
     * de la page courante.
     */
    public int getOffset() {
        return pageNumber * pageLength;
    }

    /** */
    public int getPageLength() {
        return pageLength;
    }

    /** */
    public int getPageNumber() {
        return pageNumber;
    }

    public int getTotalNumberOfElements() {
        return totalNumberOfElements;
    }

    /** */
    public void goToNextPage() {
        if (pageNumber < nbOfPages) {
            pageNumber++;
        }
    }

    /** */
    public void goToPreviousPage() {
        if (pageNumber > 0) {
            pageNumber--;
        }
    }

    /**
     * @param newLimit le nombre total d'éléments à changer.
     */
    public void setLimit(final int newLimit) {
        totalNumberOfElements = newLimit;
    }

    public void setNbOfPages(final int nbOfPages) {
        this.nbOfPages = nbOfPages;
    }

    /**
     * @param newElementsPerPage
     */
    public void setPageLength(final int newElementsPerPage) {
        pageLength = newElementsPerPage;
        nbOfPages = totalNumberOfElements / pageLength
            + (totalNumberOfElements % pageLength == 0 ? 0 : 1);
        pageNumber = nbOfPages < pageNumber ? nbOfPages - 1 : pageNumber;
    }

    public void setPageNumber(final int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setTotalNumberOfElements(final int totalNumberOfElements) {
        this.totalNumberOfElements = totalNumberOfElements;
    }

}
