package com.excilys.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/addComputer")
public class CreateComputerServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param request
     * @param response
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) {
    }

    /**
     * @param request comprenant les paramètres : computerName, introduced, discontinued,
     * companyId.
     * @param response
     */
    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response) {
    }
}
