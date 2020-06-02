package com.excilys.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.model.CompanyDTO;
import com.excilys.service.CompanyDTOValidator;

@WebServlet("/createComputer")
public class CreateComputerPage extends HttpServlet {
    private static CompanyDTOValidator companyValidator = new CompanyDTOValidator();
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * @param request
     * @param response
     */
    @Override
    public void doGet(final HttpServletRequest request,
        final HttpServletResponse response) {
        try {
            List<CompanyDTO> companyList = companyValidator.fetchList();
            request.setAttribute("companyList", companyList);
            RequestDispatcher rd = request
                .getRequestDispatcher("WEB-INF/views/addComputer.jsp");
            rd.forward(request, response);
        } catch (ServletException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
