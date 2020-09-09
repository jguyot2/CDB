package com.excilys.adapters;

public interface Validator<T, ThrownException extends Exception> {

    void validate(T instanceToValidate) throws ThrownException;
}
