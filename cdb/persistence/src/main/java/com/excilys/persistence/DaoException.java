package com.excilys.persistence;

// TODO changer le nom parce que presque la même chose que PersistenceException
public class DaoException extends Exception {

    private static final long serialVersionUID = 1L;


    public DaoException() {
        super();
    }

    public DaoException(final Throwable e) {
        super(e);
    }
}
