package com.excilys.model;

public class CompanyDTO {
	private String name;
	private String id; 
	
	public CompanyDTO(){
	}
	
	public CompanyDTO(String name) {
		this.name = name;
	}
	
	public String getName () {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
}
