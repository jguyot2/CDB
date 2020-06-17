package com.excilys.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jguyot2
 *
 */
@WebServlet("/404")
public class Error404Servlet extends HttpServlet {
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Gestion de l'erreur 404.
     *
     * @param request
     * @param response
     * @throws ServletException
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        try {
            // response.sendRedirect("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
            request.getRequestDispatcher("/WEB-INF/views/404.jsp").forward(request, response);
        } catch (IOException e) {
            throw new ServletException(e);
        }
    }
}
