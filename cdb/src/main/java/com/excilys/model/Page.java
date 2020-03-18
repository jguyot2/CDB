package com.excilys.model;

/**
 * Classe permettant la pagination. 
 * Garde l'identifiant 
 * @author jguyot2
 *
 */
public class Page {
	private int offset;
	private int limit;
	private int elemeentsPerPage = 30;
	
	public Page() {
		this.offset = 0;
		this.limit = -1;
	}
	
	public void setElementsPerPage(int newElementsPerPage) {
		this.elemeentsPerPage = newElementsPerPage;
	}
	/**
	 * Création d'une page sur un ensemble contenant <limit> éléments. 
	 * 
	 * @param limit
	 * 
	 * TODO: vérifier si c'est cohérent de garder cette limite, e.g en cas de changement de la table
	 */
	public Page(int limit) {
		this.offset = 0;
		this.limit = limit;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public boolean limitIsSet() {
		return limit != -1;
	}
	
	public int getElemeentsperpage() {
		return elemeentsPerPage;
	}
	
	public void goToNextPage() {
		this.offset += this.elemeentsPerPage;
		if(this.limitIsSet() && this.offset > this.limit)
			this.offset = this.limit - 1;
	}
	
	public void goToPreviousPage() {
		this.offset -= this.elemeentsPerPage;
		this.offset = this.offset < 0 ? 0 : this.offset;
	}
}