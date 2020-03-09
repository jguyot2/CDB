package model;

import java.util.Date;

/**
 * Classe représentant un ordinateur 
 * @author jguyot2
 *
 */
public class Computer {
	private String name;
	private Company manufacturer;
	private Date introduction;
	private Date discontinuation;
	private long id;
	
	public Computer() {
	}
	
	public Computer(String name) {
		this.name = name;
		this.manufacturer = null;
		this.introduction = null;
		this.discontinuation = null;
		this.id = 0;
	}

	public Computer(String name, 
			Company manufacturer, 
			Date introduction, 
			Date discontinuation) {
		
		this.name = name;
		this.manufacturer = manufacturer;
		this.introduction = introduction;
		this.discontinuation = discontinuation;
		this.id = 0;
	}
	public Computer(String name, 
			Company manufacturer, 
			Date introduction, 
			Date discontinuation,
			long id) {
		
		this.name = name;
		this.manufacturer = manufacturer;
		this.introduction = introduction;
		this.discontinuation = discontinuation;
		this.id = id;
	}

	public String getName() {
		return name;
	}


	public Company getManufacturer() {
		return manufacturer;
	}

	public Date getIntroduction() {
		return introduction;
	}
	
	public Date getDiscontinuation() {
		return discontinuation;
		
	}
	
	@Override
	public String toString() {
		String representation = "Computer( ";
		representation += "name=" + this.name + "\t";
		representation += "manufacturer=" + String.valueOf(this.manufacturer)+"\t";
		representation += "intro=" + String.valueOf(this.introduction) + "\t";
		representation += "dicontinuation=" + String.valueOf(this.discontinuation)+")";
		return representation;
	}

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setManufacturer(Company manufacturer) {
		this.manufacturer = manufacturer;
	}

	public void setIntroduction(Date introduction) {
		this.introduction = introduction;
	}

	public void setDiscontinuation(Date discontinuation) {
		this.discontinuation = discontinuation;
	}
}
