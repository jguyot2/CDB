package com.excilys.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.model.User;


@Repository
public class UserUpdater {

    @PersistenceContext
    EntityManager em;


    @Transactional(rollbackFor = DaoException.class)
    public boolean createUser(@NonNull final User newUser) throws DaoException {
        try {
            this.em.merge(newUser);
            return true;
        } catch (PersistenceException e) {
            throw new DaoException(e);
        }
    }
}
