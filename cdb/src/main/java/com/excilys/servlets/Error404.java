package com.excilys.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/404")
public class Error404 extends HttpServlet {
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Gestion de l'erreur 404.
     *
     * @param request
     * @param response
     */
    @Override
    public void doGet(final HttpServletRequest request,
        final HttpServletResponse response) {
        try {
            response.sendRedirect("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
        } catch (IOException e) {
        }
    }
}
