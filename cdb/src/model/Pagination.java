package model;

public class Pagination {
	private int offset;
	private int limit;
	private final int elemeentsPerPage = 30;
	
	public Pagination() {
		this.offset = 0;
		this.limit = -1;
	}

	public Pagination(int limit) {
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
		if(this.limitIsSet())
			this.offset = this.limit - 1;
	}
	
	public void goToPreviousPage() {
		this.offset -= this.elemeentsPerPage;
		this.offset = this.offset < 0 ? 0 : this.offset;
	}
}
