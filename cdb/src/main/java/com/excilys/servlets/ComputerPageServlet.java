package com.excilys.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.model.ComputerDTO;
import com.excilys.model.Page;
import com.excilys.service.ComputerDTOValidator;

@WebServlet("/page")
public class ComputerPageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    // TODO : renommer ça ?
    private static final ComputerDTOValidator validator = new ComputerDTOValidator();

    /**
     * Envoi d'une page représentant un certain nombre d'instances de Computer
     * @param request la requête, qui peut contenir les params suivants :
     * "pageNumber" représentant le numéro de page courante (Première page par défaut)
     * "pageLength" Représentant le nombre d'éléments par page.
     * @throws ServletException
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException {
        try {
            Page page = this.getPageFromRequest(request);
            setAttributesFromPage(request, page);
            RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/views/dashboard.jsp");
            rd.forward(request, response);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServletException(e);
        } catch (NumberFormatException e) {
            RequestDispatcher rd = request.getRequestDispatcher("/400");
            request.setAttribute("errorCause",
                "the page number or page length parameter is invalid");
            try {
                rd.forward(request, response);
            } catch (IOException e1) {
                throw new ServletException(e1);
            }
        }
    }

    private Page getPageFromRequest(final HttpServletRequest request) throws NumberFormatException {
        String strPageNumber = request.getParameter("pageNumber");
        String strPageLength = request.getParameter("pageLength");
        Page page = new Page();
        if (strPageNumber != null) {
            page.setPageNumber(Integer.parseInt(strPageNumber));
        }
        if (strPageLength != null) {
            page.setPageLength(Integer.parseInt(strPageLength));
        }
        return page;
    }

    private void setAttributesFromPage(final HttpServletRequest request, final Page page) {
        request.setAttribute("pageNumber", page.getPageNumber());
        request.setAttribute("pageLength", page.getPageLength());
        List<ComputerDTO> computerList = validator.fetchWithOffset(page);
        request.setAttribute("computerList", computerList);
    }
}
