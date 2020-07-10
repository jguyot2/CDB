package com.excilys.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import com.excilys.model.User;
import com.excilys.persistence.PersistanceException;
import com.excilys.persistence.UserSearcher;
import com.excilys.persistence.UserUpdater;

@Service
public class UserService {
    @Autowired
    private UserSearcher searcher;
    @Autowired
    private UserUpdater updater;

    @Autowired
    private UserValidator validator;

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public Optional<User> getUserDetails(@Nullable final String username) {
        try {
            if (username == null || username.isEmpty()) {
                return Optional.empty();
            } else {
                return this.searcher.getUserDetails(username);
            }
        } catch (final PersistanceException e) {
            LOG.error("Persistence exception found", e);
            return Optional.empty();
        }
    }

    public boolean addUser(@Nullable final User user) throws InvalidUserException {
        this.validator.validate(user);
        if (user == null) {
            LOG.info("null user added in paramter");
            return false;
        }
        try {
            return this.updater.createUser(user);
        } catch (final PersistanceException e) {
            LOG.error("db error", e);
            return false;
        }
    }
}
