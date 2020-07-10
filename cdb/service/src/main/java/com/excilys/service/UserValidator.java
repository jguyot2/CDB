package com.excilys.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.excilys.adapters.Validator;
import com.excilys.model.User;

@Component
public class UserValidator implements Validator<User, InvalidUserException> {
    @Override
    public void validate(final User user) throws InvalidUserException {
        if (user == null) {
            throw new InvalidUserException(Arrays.asList(UserProblems.NULL_VALUE));
        }
        final List<UserProblems> problems = new ArrayList<>();
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            problems.add(UserProblems.INVALID_NAME);
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            problems.add(UserProblems.INVALID_PASSWORD);
        }
        if (!problems.isEmpty()) {
            throw new InvalidUserException(problems);
        }
    }
}
