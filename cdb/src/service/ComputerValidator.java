package service;

import model.Computer;
import persistence.ComputerUpdater;

public class ComputerValidator {

	public static boolean isValidComputerInstance(Computer computer) {
		if (computer.getName() == null)
			return false;

		if (computer.getIntroduction() != null) {
			if (computer.getDiscontinuation() == null)
				return true;
			return computer.getIntroduction().compareTo(computer.getDiscontinuation()) <= 0;
		}
		
		return computer.getDiscontinuation() == null;

	}

	public static boolean updateComputer(long id, Computer newComputervalue) {
		if(! isValidComputerInstance(newComputervalue))
			return false;
		else return ComputerUpdater.updateComputerById(id, newComputervalue);
	}
	
	public static boolean deleteComputer(long id) {
		return ComputerUpdater.deleteComputerById(id);
	}
	
	public static boolean createComputer(Computer createdComputer) {
		if(! isValidComputerInstance(createdComputer))
			return false;
		return ComputerUpdater.createComputer(createdComputer);
	}
}
