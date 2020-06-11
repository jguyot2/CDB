package com.excilys.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private static final ComputerDTOValidator validator = new ComputerDTOValidator();

    /**
     * Envoi d'une page représentant un certain nombre d'instances de Computer
     *
     * @param request la requête, qui peut contenir les params suivants :
     *        "pageNumber" représentant le numéro de page courante (Première page par
     *        défaut) "pageLength" Représentant le nombre d'éléments par page.
     * @throws ServletException
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        try {
            Page page = getPageFromRequest(request);
            setAttributesFromPage(request, page);
            RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/views/dashboard.jsp");
            rd.forward(request, response);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServletException(e);
        } catch (NumberFormatException e) {
            RequestDispatcher rd = request.getRequestDispatcher("/400");
            request.setAttribute("errorCause", "the page number or page length parameter is invalid");
            try {
                rd.forward(request, response);
            } catch (IOException e1) {
                throw new ServletException(e1);
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, String[]> params = request.getParameterMap();

        StringBuilder message = new StringBuilder("");
        for (String param : params.keySet()) {
            String[] values = params.get(param);

            message.append(param).append("= ");
            for (String val : values) {
                message.append(val + " , ");
            }
            message.append(" <br/>");
        }

        request.setAttribute("errorCause", message.toString());
        request.getRequestDispatcher("/400").forward(request, response);

    }

    /**
     * A partir de la requête en paramètre, crée un objet Page correspondant
     *
     * @param request la requête
     * @return
     * @throws NumberFormatException si les paramètres ne correspondent pas à des
     *         nombres
     */
    private Page getPageFromRequest(final HttpServletRequest request) throws NumberFormatException {
        String strPageNumber = request.getParameter("pageNumber");
        String strPageLength = request.getParameter("pageLength");
        Page page = new Page();
        page.setTotalNumberOfElements(validator.getNumberOfElements());

        if (strPageNumber != null) {
            page.setPageNumber(Integer.parseInt(strPageNumber));
        }
        if (strPageLength != null) {
            page.setPageLength(Integer.parseInt(strPageLength));
        }

        return page;
    }

    /**
     * Ajout des attributs qui seront passés au .jsp à la requête courante
     *
     * @param request
     * @param page
     */
    private void setAttributesFromPage(final HttpServletRequest request, final Page page) {
        List<ComputerDTO> computerList = validator.fetchWithOffset(page);

        List<Integer> pagesToShow = new ArrayList<>();
        int firstPageToShow = Math.max(0, page.getPageNumber() - 2);
        int nbPages = page.getNbOfPages();
        for (int i = 0; (i < 5) && ((firstPageToShow + i) < nbPages); ++i) {
            pagesToShow.add(firstPageToShow + i);
        }

        request.setAttribute("page", page);
        request.setAttribute("computerList", computerList);
        request.setAttribute("pageList", pagesToShow);
    }
}
