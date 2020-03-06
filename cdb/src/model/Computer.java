package model;

import java.util.Date;
import java.util.Optional;

public class Computer {
	private String name;
	private Optional<Company> manufacturer;
	private Optional<Date> introduction;
	private Optional<Date> discontinuation;
	
	public Computer(String name) {
		this.name = name;
		this.manufacturer = Optional.empty();
		this.introduction = Optional.empty();
		this.discontinuation = Optional.empty();
	}
	
	public Computer(String name, 
			Optional<Company> manufacturer, 
			Optional<Date> introduction, 
			Optional<Date> discontinuation) {
		this.name = name;
		this.manufacturer = manufacturer;
		this.introduction = introduction;
		this.discontinuation = discontinuation;
	}

	public String getName() {
		return name;
	}


	public Optional<Company> getManufacturer() {
		return manufacturer;
	}

	public Optional<Date> getIntroduction() {
		return introduction;
	}
	
	public Optional<Date> getDiscontinuation() {
		return discontinuation;
		
	}
	
	@Override
	public String toString() {
		
	}
}
