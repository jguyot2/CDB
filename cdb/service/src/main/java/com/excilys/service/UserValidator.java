package com.excilys.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.excilys.adapters.Validator;
import com.excilys.model.User;


/**
 * Classe utilisée pour vérifier la validité d'un utilisateur.
 *
 * @author jguyot2
 */
@Component
public class UserValidator implements Validator<User, InvalidUserException> {

    /**
     * Validation d'une instance d'utilisateur : Les nom et mot de passe ne doivent pas être nuls ou vides.
     *
     * @param user l'utilisateur à valider
     *
     * @throws InvalidUserException si l'instance en paramètre est nulle ou invalide
     */
    @Override
    public void validate(@Nullable final User user) throws InvalidUserException {
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
