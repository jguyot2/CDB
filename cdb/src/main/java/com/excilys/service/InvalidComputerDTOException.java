package com.excilys.service;

import java.util.List;

public class InvalidComputerDTOException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    List<ComputerDTOProblems> problems;

    public InvalidComputerDTOException(final List<ComputerDTOProblems> problemList) {
        super();
        problems = problemList;
    }

    public List<ComputerDTOProblems> getProblems() {
        return problems;
    }
}
