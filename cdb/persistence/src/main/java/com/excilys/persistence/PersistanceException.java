package com.excilys.persistence;

public class PersistanceException extends Exception {
    private static final long serialVersionUID = 1L;

    public PersistanceException(final Throwable e) {
        super(e);
    }

    public PersistanceException() {
        super();
    }
}
