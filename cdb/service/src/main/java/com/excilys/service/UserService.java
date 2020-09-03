package com.excilys.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.excilys.mapper.UserMapper;
import com.excilys.model.User;
import com.excilys.persistence.DaoException;
import com.excilys.persistence.UserSearcher;
import com.excilys.persistence.UserUpdater;

@Service
public class UserService {
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private UserSearcher searcher;

	@Autowired
	private UserUpdater updater;

	@Autowired
	private UserValidator validator;

	public boolean create(@Nullable final User user) throws InvalidUserException {
		this.validator.validate(user);
		try {
			return this.updater.createUser(user);
		} catch (final DaoException e) {
			LOG.error("db error", e);
			return false;
		}
	}

	public Optional<User> getUserDetails(@Nullable final String username) {
		try {
			if (username == null || username.isEmpty()) {
				return Optional.empty();
			} else {
				return this.searcher.getUserDetails(username);
			}
		} catch (final DaoException e) {
			LOG.error("Persistence exception found", e);
			return Optional.empty();
		}
	}

	void setSearcher(final UserSearcher newSearcher) {
		this.searcher = newSearcher;
	}

	void setUpdater(final UserUpdater newUpdater) {
		this.updater = newUpdater;
	}

	public UserDetails loadUserByUsername(final String username) {
		return UserMapper.toDto(getUserDetails(username).orElseThrow(IllegalArgumentException::new)).get();
	}
}
