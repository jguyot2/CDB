package com.excilys.restcontrollers.security.dto;

public class JwtRequest {
	private String username;
	private String password;

	// need default constructor for JSON Parsing
	public JwtRequest() {

	}

	public JwtRequest(final String username, final String password) {
		setUsername(username);
		setPassword(password);
	}

	public String getPassword() {
		return this.password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public void setUsername(final String username) {
		this.username = username;
	}
}
