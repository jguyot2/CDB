package com.excilys.service;

import java.util.List;

public class InvalidComputerInstanceException extends Exception {
	private static final long serialVersionUID = 1L;
	private List<ComputerInstanceProblems> problems;
	
	public InvalidComputerInstanceException(List<ComputerInstanceProblems> problems) {
		this.problems = problems;
	}

	public List<ComputerInstanceProblems> getProblems(){
		return this.problems;
	}
}
