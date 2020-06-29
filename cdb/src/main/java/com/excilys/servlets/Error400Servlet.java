package com.excilys.servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Error400Servlet {
    private static final Logger LOG = LoggerFactory.getLogger(Error400Servlet.class);

    private static final long serialVersionUID = 1L;

    @RequestMapping("/400")
    public String showError400(
            @RequestParam(required = false, name = "errorCause") final String errorCause,
            final Model m) {
        m.addAttribute("errorCause", errorCause);
        return "400";
    }
}
