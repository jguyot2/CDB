package com.excilys.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultServlet extends HttpServlet {
 
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            response.sendRedirect("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
