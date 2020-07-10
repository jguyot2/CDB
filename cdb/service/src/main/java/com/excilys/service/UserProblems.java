package com.excilys.service;

public enum UserProblems {
    NULL_VALUE("the value is null"), INVALID_NAME("invalid name"), INVALID_PASSWORD("invalid password");

    private String msg;

    private UserProblems(final String message) {
        this.msg = message;
    }

    public String getExplanation() {
        return this.msg;
    }
}
