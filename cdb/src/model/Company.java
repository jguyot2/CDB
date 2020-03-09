package model;


/**
 * Classe représentant une entreprise par son nom
 * @author jguyot2
 *
 */
public class Company {
	private final String name;
	private long id;
	
	public Company(String name) {
		this.name = name;
	}
	public Company(String name, long id) {
		this.name = name;
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "Company(" + String.valueOf(this.name) + ")";
	}
}
