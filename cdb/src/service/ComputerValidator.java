package service;

import java.util.List;
import java.util.Optional;

import model.Computer;
import persistence.ComputerSearcher;
import persistence.ComputerUpdater;

public class ComputerValidator {

	public static boolean isValidComputerInstance(Computer computer) {

		if (computer.getName() == null || computer.getName().trim().isEmpty())
			return false;
		
		if (computer.getIntroduction() != null) {
			if (computer.getDiscontinuation() == null)
				return true;
			return computer.getIntroduction().compareTo(computer.getDiscontinuation()) <= 0;
		}
		return computer.getDiscontinuation() == null;
	}

	// Pê mettre des exceptions permettant de préciser la cause du non-update
	public static boolean updateComputer(long id, Computer newComputervalue) {
		if (!isValidComputerInstance(newComputervalue))
			return false;
		else
			return ComputerUpdater.updateComputerById(id, newComputervalue);
	}

	public static boolean deleteComputer(long id) {
		return ComputerUpdater.deleteComputerById(id);
	}

	public static long createComputer(Computer createdComputer) {
		if (!isValidComputerInstance(createdComputer))
			return 0;
		return ComputerUpdater.createComputer(createdComputer);
	}

	public static Optional<Computer> fetchComputerById(long id) {
		return ComputerSearcher.fetchComputerById(id);
	}

	public static List<Computer> fetchComputerList() {
		return ComputerSearcher.fetchComputerList();
	}
}
