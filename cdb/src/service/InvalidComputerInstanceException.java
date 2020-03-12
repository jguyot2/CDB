package service;

import java.util.List;

public class InvalidComputerInstanceException extends Exception {
	private List<ComputerInstanceProblems> problems;
	public InvalidComputerInstanceException(List<ComputerInstanceProblems> problems) {
		this.problems = problems;
	}
	
	public List<ComputerInstanceProblems> getProblems(){
		return this.problems;
	}
}
