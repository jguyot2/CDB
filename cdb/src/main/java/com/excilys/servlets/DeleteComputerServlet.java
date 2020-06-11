package com.excilys.servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteComputerServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, String[]> params = request.getParameterMap();

        StringBuilder message = new StringBuilder("");
        for (String param : params.keySet()) {
            String[] values = params.get(param);
            message.append(param).append("= ").append(values);
            message.append(" <br/>\n");
        }

        request.setAttribute("errorCause", message.toString());
        request.getRequestDispatcher("/400").forward(request, response);

    }
}
