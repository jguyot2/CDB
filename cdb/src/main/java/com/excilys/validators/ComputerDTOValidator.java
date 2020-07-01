package com.excilys.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ComputerDTOValidator implements Validator {

    @Override
    public boolean supports(final Class<?> clazz) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        // TODO Auto-generated method stub

    }
}
