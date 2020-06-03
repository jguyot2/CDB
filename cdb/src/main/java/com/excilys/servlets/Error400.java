package com.excilys.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/400")
public class Error400 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
    * @param request comprendant un attribut optionnel "errorCause", qui décrit la cause de l'erreur.
     * @throws ServletException 
    */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        String errorCause = (String) request.getAttribute("errorCause");
        if(errorCause != null) {
            String htmlErrorCause = errorCause.replace("\n", "<br/>");
            request.setAttribute("errorCause", htmlErrorCause);
        }
        
        RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/views/400.jsp");
        try {
            rd.forward(request, response);

        } catch (IOException e) {
          throw new ServletException(e);
        }
    }

    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        doGet(request, response);
    }
}
