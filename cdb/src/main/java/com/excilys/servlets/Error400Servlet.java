package com.excilys.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet correspondant à l'erreur 400 (= l'utilisateur fait de la merde)
 * @author jguyot2
 *
 */
@WebServlet("/400")
public class Error400Servlet extends HttpServlet {
    /** */
    private static final Logger LOG = LoggerFactory.getLogger(Error400Servlet.class);

    private static final long serialVersionUID = 1L;

    /**
     * @param request comprendant un attribut optionnel "errorCause", qui décrit la
     *        cause de l'erreur.
     * @throws ServletException
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        String errorCause = (String) request.getAttribute("errorCause");
        if (errorCause != null) {
            String htmlErrorCause = errorCause.replace("\n", "<br/>");
            request.setAttribute("errorCause", htmlErrorCause);
        }

        RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/views/400.jsp");
        try {
            rd.forward(request, response);

        } catch (IOException e) {
            LOG.error(e.getMessage());
            throw new ServletException(e);
        }
    }

    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        doGet(request, response);
    }
}
