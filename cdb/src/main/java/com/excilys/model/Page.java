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
    private int totalNumberOfElements;
    
    /** */
    private int pageLength = BASE_NUMBER_OF_ELEMENTS_PER_PAGE;
    
    /** */
    private int nbOfPages;

    /** commence à 0 */
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
    public Page() {}
    
    public Page(int numberOfElements, int pageNumber, int elementsPerPage) {
        throw new NotImplementedException();
    }

    /** */
    public int getPageNumber() {
        return this.pageNumber;
    }

    /** */
    public int getNbOfPAges() {
        return this.nbOfPages;
    }

    /** */
    public int getPageLength() {
        return pageLength;
    }

    /** */
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

    /** */
    public void goToNextPage() {
        if (this.pageNumber < this.nbOfPages) {
            this.pageNumber++;
        }
    }

    /** */
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
    public int getTotalNumberOfElements() {
        return totalNumberOfElements;
    }
    public void setTotalNumberOfElements(int totalNumberOfElements) {
        this.totalNumberOfElements = totalNumberOfElements;
    }
    public int getNbOfPages() {
        return nbOfPages;
    }
    public void setNbOfPages(int nbOfPages) {
        this.nbOfPages = nbOfPages;
    }
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
  
}
