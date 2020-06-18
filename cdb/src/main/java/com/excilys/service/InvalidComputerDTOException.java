package com.excilys.service;

import java.util.List;

/**
 * Exception lancée pour indiquer qu'une instance de ComputerDTO passée en
 * paramètre est invalide.
 *
 * @author jguyot2
 *
 */
public class InvalidComputerDTOException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    List<ComputerDTOProblems> problems;

    /**
     *
     * @param problemList la liste des problèmes associés au DTO.
     */
    public InvalidComputerDTOException(final List<ComputerDTOProblems> problemList) {
        super();
        this.problems = problemList;
    }

    public List<ComputerDTOProblems> getProblems() {
        return this.problems;
    }
}
