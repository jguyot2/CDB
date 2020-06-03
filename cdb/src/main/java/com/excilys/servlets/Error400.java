package com.excilys.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO : Pê dégager ce servlet là
 * @author jguyot2
 *
 */
@WebServlet("/400")
public class Error400 extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
    * @param
    */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) {

    }

    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response) {
        doGet(request, response);
    }
}
