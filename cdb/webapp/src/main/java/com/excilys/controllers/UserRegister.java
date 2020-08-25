package com.excilys.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.adapters.UserDtoAdapter;
import com.excilys.model.UserDto;
import com.excilys.model.UserRoles;
import com.excilys.service.InvalidUserException;
import com.excilys.service.UserProblems;

@Controller
@CrossOrigin
public class UserRegister {

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	UserDtoAdapter adapter;

	@GetMapping("/addUser")
	public String addUser() {
		return "addUser";
	}

	@PostMapping("/addUser")
	public String registerUser(@RequestParam("username") final String username,
			@RequestParam("password") final String password, final Model m) {
		UserDto createdUser = new UserDto();

		String encodedPassword = this.encoder.encode(password);
		createdUser.setUsername(username);
		createdUser.setPassword(encodedPassword);
		createdUser.setRole(UserRoles.USER);
		try {
			this.adapter.addUser(createdUser);
			return "redirect:/page";
		} catch (final InvalidUserException e) {
			final StringBuilder sb = new StringBuilder();
			for (final UserProblems cause : e.getProblems()) {
				sb.append(cause.getExplanation() + " <br/> \n");
			}
			m.addAttribute("errorCause", sb.toString());
			return "400";
		}
	}
}
