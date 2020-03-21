package com.excilys.servlets;

import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.model.Computer;
import com.excilys.service.ComputerValidator;

@WebServlet("/list")
public class ShowComputerListServlet extends HttpServlet {
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * @param request
     * @param response
     */
    public void doGet(final HttpServletRequest request,
            final HttpServletResponse response) throws ServletException {
        try {
            ComputerValidator computerValidator = new ComputerValidator();
            List<Computer> computerList = computerValidator.fetchList();
            request.setAttribute("computerList", computerList);
            RequestDispatcher rd = request
                    .getRequestDispatcher("/WEB-INF/views/dashboard.jsp");
            rd.forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
