package model;

import java.time.LocalDate;

/**
 * Classe représentant un ordinateur
 * 
 * @author jguyot2
 *
 */
public class Computer {
	private String name;
	private Company manufacturer;
	private LocalDate introduction;
	private LocalDate discontinuation;
	private long id;

	public Computer() {}

	public Computer(String name) {
		this.name = name;
		this.manufacturer = null;
		this.introduction = null;
		this.discontinuation = null;
		this.id = 0;
	}

	public Computer(String name, Company manufacturer, LocalDate introduction, LocalDate discontinuation) {

		this.name = name;
		this.manufacturer = manufacturer;
		this.introduction = introduction;
		this.discontinuation = discontinuation;
		this.id = 0;
	}

	public Computer(String name, Company manufacturer, LocalDate introduction, LocalDate discontinuation, long id) {

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

	public LocalDate getIntroduction() {
		return introduction;
	}

	public LocalDate getDiscontinuation() {
		return discontinuation;

	}

	@Override
	public String toString() {
		String representation = "";
		representation += "name=" + this.name + "\t";
		representation += "manufacturer=" + String.valueOf(this.manufacturer) + "\t";
		representation += "intro=" + String.valueOf(this.introduction) + "\t";
		representation += "dicontinuation=" + String.valueOf(this.discontinuation);
		return representation;
	}

	/**
	 * Décrit brièvement une instance de Computer
	 * @return une chaîne contenant l'id et le nom de l'instance courante
	 */
	public String getShortDescription() {
		return "("+this.id+", " + this.name + " )";
	}

	public long getId() {
		return id;
	}

	public void setId(long l) {
		this.id = l;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setManufacturer(Company manufacturer) {
		this.manufacturer = manufacturer;
	}

	public void setIntroduction(LocalDate introduction) {
		this.introduction = introduction;
	}

	public void setDiscontinuation(LocalDate discontinuation) {
		this.discontinuation = discontinuation;
	}
}
