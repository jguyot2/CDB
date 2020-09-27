package com.excilys.queryparamparsing.ast;

public enum LogicalOperator {
    AND, OR;

    public static LogicalOperator getFromString(final String s) {
        switch (s) {
        case "$and":
        case "and":
            return AND;

        case "$or":
        case "or":
            return OR;

        default:
            throw new IllegalArgumentException();
        }
    }
}
