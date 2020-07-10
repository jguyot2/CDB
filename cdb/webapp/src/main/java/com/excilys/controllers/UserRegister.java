package com.excilys.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.adapters.UserDtoAdapter;
import com.excilys.model.UserDto;
import com.excilys.model.UserRoles;
import com.excilys.service.InvalidUserException;
import com.excilys.service.UserProblems;

@Controller
public class UserRegister {

    @Autowired
    UserDtoAdapter adapter;

    @Autowired
    PasswordEncoder encoder;

    @GetMapping("/addUser")
    public String addUser() {
        return "addUser";
    }

    @PostMapping("/addUser")
    public String registerUser(@RequestParam("username") final String username,
            @RequestParam("password") final String password, final Model m) {
        UserDto createdUser = new UserDto();
        createdUser.setUsername(username);
        String encryptedPassword = this.encoder.encode(password);
        createdUser.setPassword(encryptedPassword);
        createdUser.setRole(UserRoles.ROLE_USER);
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
