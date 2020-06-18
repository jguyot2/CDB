package com.excilys.service;

import java.util.List;

/**
 * Exception lancée lors de la rencontre d'une instance de Computer invalide.
 * Contient une liste de ComputerInstanceProblems indiquant le ou les problèmes
 * associées à une instance
 *
 * @author jguyot2
 *
 */
public class InvalidComputerInstanceException extends Exception {
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Liste des problèmes associés à une instance de Computer donnée.
     */
    private List<ComputerInstanceProblems> problems;

    /**
     * @param problemList Les problèmes associés à une instance de Computer donnée
     */
    public InvalidComputerInstanceException(final List<ComputerInstanceProblems> problemList) {
        this.problems = problemList;
    }

    /**
     * @return La liste des problèmes d'une instance donnée.
     */
    public List<ComputerInstanceProblems> getProblems() {
        return this.problems;
    }
}
