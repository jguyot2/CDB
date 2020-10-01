package com.excilys.adapters;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.excilys.mapper.UserMapper;
import com.excilys.model.User;
import com.excilys.model.UserDto;
import com.excilys.service.InvalidUserException;
import com.excilys.service.UserService;

@Service
public class UserDtoAdapter implements UserDetailsService {

	@Autowired
	private UserService service;

	public boolean addUser(final UserDto user) throws InvalidUserException {
		if (user == null) {
			return false;
		}
		return this.service.create(UserMapper.fromDto(user).orElseThrow(RuntimeException::new));
	}

	@Override
	public UserDetails loadUserByUsername(@Nullable final String username) throws UsernameNotFoundException {
		if (username == null || username.isEmpty()) {
			throw new UsernameNotFoundException("null or empty username");
		}
		final Optional<User> s = this.service.getUserDetails(username);
		if (s.isPresent()) {
			return UserMapper.toDto(s.orElseThrow(RuntimeException::new)).orElseThrow(RuntimeException::new);
		} else {
			throw new UsernameNotFoundException("user=" + username);
		}
	}
}
