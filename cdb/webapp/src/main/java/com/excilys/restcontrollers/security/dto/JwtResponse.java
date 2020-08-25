package com.excilys.restcontrollers.security.dto;

public class JwtResponse {
	private final String jwt;
	private final String role;

	public JwtResponse(final String jwt, final String role) {
		this.jwt = jwt;
		this.role = role;
	}

	public String getJwt() {
		return this.jwt;
	}

	public String getRole() {
		return this.role;
	}
}
