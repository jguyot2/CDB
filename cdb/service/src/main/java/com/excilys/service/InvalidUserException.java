package com.excilys.service;

import java.util.List;

public class InvalidUserException extends Exception {
    private static final long serialVersionUID = 1L;
    private List<UserProblems> problems;

    public InvalidUserException(final List<UserProblems> problems) {
        this.problems = problems;
    }

    public List<UserProblems> getProblems() {
        return this.problems;
    }
}
