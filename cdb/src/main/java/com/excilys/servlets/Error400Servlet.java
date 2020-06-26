package com.excilys.servlets;

import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class Error400Servlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(Error400Servlet.class);

    private static final long serialVersionUID = 1L;

    @GetMapping
    @PostMapping
    public String showError400(
            @RequestParam(required = false, name = "errorCause") final String errorCause,
            final Model m) {
        m.addAttribute("errorCause", errorCause);
        return "400";
    }
}
