package com.excilys.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.model.ComputerDTO;
import com.excilys.model.Page;
import com.excilys.service.ComputerDTOValidator;

public class ComputerPageServlet extends HttpServlet {
    private static final ComputerDTOValidator validator = new ComputerDTOValidator();

    /**
     * Envoi d'une page représentant un certain nombre d'instances de Computer
     * @param request la requête, qui peut contenir les params suivants :
     * "pageNumber" représentant le numéro de page courante (Première page par défaut)
     * "pageLength" Représentant le nombre d'éléments par page.
     * @throws ServletException 
     */
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        try {
            String strPageNumber = request.getParameter("pageNumber");
            String strPageLength = request.getParameter("pageLength");
            Page page = new Page();
            try {
                if (strPageNumber != null)
                    page.setPageNumber(Integer.parseInt(strPageNumber));
                
                if (strPageLength != null)
                    page.setPageLength(Integer.parseInt(strPageLength));

                request.setAttribute("pageNumber", page.getPageNumber());
                request.setAttribute("pageLength", page.getPageLength());
            } catch (NumberFormatException e) {
                throw new RuntimeException(); //TODO 
            }
            List<ComputerDTO> computerList = validator.fetchWithOffset(page);
            request.setAttribute("computerList", computerList);
            RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/dashboardPage.jsp");

            rd.forward(request, response);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}
