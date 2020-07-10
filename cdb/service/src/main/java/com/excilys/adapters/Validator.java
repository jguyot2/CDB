package com.excilys.adapters;

// TODO : utiliser ça ?
public interface Validator<T, ThrownException extends Exception> {
    public void validate(T instanceToValidate) throws ThrownException;
}
