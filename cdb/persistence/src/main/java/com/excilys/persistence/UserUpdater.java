package com.excilys.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.dao.DataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.model.User;

@Repository
public class UserUpdater {

	@PersistenceContext
	EntityManager em;

	@Transactional(rollbackFor = PersistanceException.class)
	public boolean createUser(@NonNull final User newUser) throws PersistanceException {
		try {
			this.em.merge(newUser);
			return true;
		} catch (DataAccessException e) {
			throw new PersistanceException(e);
		}
	}
}
