package model;

import java.util.Date;
import java.util.Optional;

public class Computer {
	private String name;
	private Optional<String> manufacturer;
	private Optional<Date> introduction;
	private Optional<Date> discontinuation;
	
	public Computer(String name) {
		this.name = name;
	}
	// ... 
}
