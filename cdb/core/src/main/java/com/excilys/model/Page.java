package com.excilys.model;

/**
 * Classe permettant la pagination. Garde l'identifiant
 *
 * @author jguyot2
 */
public final class Page {
	private static final int BASE_NUMBER_OF_ELEMENTS_PER_PAGE = 10;

	private int pageLength = BASE_NUMBER_OF_ELEMENTS_PER_PAGE;

	/** commence à 0. */
	private int pageNumber = 0;
	private int totalNumberOfElements;

	public Page() {
	}

	public Page(final int numberOfElements) {
		this.totalNumberOfElements = numberOfElements;
	}

	public Page(final int numberOfElements, final int pageNumber, final int elementsPerPage) {
		this.totalNumberOfElements = numberOfElements;
		this.pageNumber = pageNumber;
		this.pageLength = elementsPerPage;
	}

	public int getLimit() {
		return this.totalNumberOfElements;
	}

	public int getNbOfPages() {
		return this.totalNumberOfElements / this.pageLength + (this.totalNumberOfElements % this.pageLength == 0 ? 0 : 1);
	}

	/**
	 * @return le nombre d'éléments avant le premier élément de la page courante.
	 *         Utilisé notamment dans la persistence
	 */
	public int getOffset() {
		return this.pageNumber * this.pageLength;
	}

	public int getPageLength() {
		return this.pageLength;
	}

	public int getPageNumber() {
		return this.pageNumber;
	}

	public int getTotalNumberOfElements() {
		return this.totalNumberOfElements;
	}

	public void goToNextPage() {
		if (this.pageNumber < getNbOfPages()) {
			this.pageNumber++;
		}
	}

	public void goToPreviousPage() {
		if (this.pageNumber > 0) {
			this.pageNumber--;
		}
	}

	public void setLimit(final int newLimit) {
		this.totalNumberOfElements = newLimit;
	}

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

	@Override
	public String toString() {
		return "page n°" + this.pageNumber + "/pageSize = " + this.pageLength + "/number of elements"
				+ this.totalNumberOfElements;
	}
}
