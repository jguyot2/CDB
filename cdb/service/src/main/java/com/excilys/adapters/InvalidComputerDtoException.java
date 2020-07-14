package com.excilys.adapters;

import java.util.List;

import org.springframework.lang.NonNull;

/**
 * Exception lancée pour indiquer qu'une instance de ComputerDTO passée en paramètre est invalide.
 *
 * @author jguyot2
 *
 */
public class InvalidComputerDtoException extends Exception {

    private static final long serialVersionUID = 1L;
    List<ComputerDTOProblems> problems;

    /**
     * @param problemList la liste des problèmes associés au DTO.
     */
    public InvalidComputerDtoException(@NonNull final List<ComputerDTOProblems> problemList) {
        super();
        this.problems = problemList;
    }

    public List<ComputerDTOProblems> getProblems() {
        return this.problems;
    }
}
