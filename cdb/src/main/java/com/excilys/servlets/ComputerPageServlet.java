package com.excilys.servlets;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.model.ComputerDTO;
import com.excilys.model.IllegalCriteriaStringException;
import com.excilys.model.Page;
import com.excilys.model.SortEntry;
import com.excilys.service.ComputerDTOValidator;
import com.excilys.service.DuplicatedSortEntries;

// TODO : Refactorisation de tout le fichier
@WebServlet("/page")
public class ComputerPageServlet extends HttpServlet {
    private static final String CHARSET = "UTF-8";
    private static final long serialVersionUID = 1L;
    private static Logger LOG = LoggerFactory.getLogger(ComputerPageServlet.class);
    private static final ComputerDTOValidator validator = new ComputerDTOValidator();

    /**
     * A partir de la requête en paramètre, crée un objet Page correspondant
     *
     * @param request la requête
     * @return
     * @throws NumberFormatException si les paramètres ne correspondent pas à des
     *         nombres
     */
    private static Page getPageFromRequest(final HttpServletRequest request, String search)
            throws NumberFormatException {
        String strPageNumber = request.getParameter("pageNumber");
        String strPageLength = request.getParameter("pageLength");
        Page page = new Page();
        if (search == null) {
            page.setTotalNumberOfElements(validator.getNumberOfElements());
        } else {
            page.setTotalNumberOfElements(validator.getNumberOfFoundElements(search));
        }

        if (strPageNumber != null) {
            page.setPageNumber(Integer.parseInt(strPageNumber));
        }
        if (strPageLength != null) {
            page.setPageLength(Integer.parseInt(strPageLength));
        }
        return page;
    }

    private static List<SortEntry> parseOrderParameters(String parameter)
            throws IllegalArgumentException, IllegalCriteriaStringException {
        if (parameter == null) {
            return Arrays.asList();
        }
        String[] orderParameter = parameter.split(",");
        LOG.info("order parameter" + orderParameter);

        if (orderParameter == null) {
            return Arrays.asList();
        }
        List<SortEntry> entries = new ArrayList<>();
        for (String param : orderParameter) {
            entries.add(SortEntry.fromString(param));
        }
        return entries;
    }

    private static List<ComputerDTO> getComputerListFromParameters(final Page page, String search,
            String sortParameters) throws DuplicatedSortEntries, IllegalCriteriaStringException {
        List<SortEntry> sortEntries = parseOrderParameters(sortParameters);
        if (search != null) {
            return validator.fetchList(page, search, sortEntries);
        } else {
            return validator.fetchList(page, sortEntries);
        }
    }

    /**
     * Ajout des attributs qui seront passés au .jsp à la requête courante
     *
     * @param request
     * @param page
     * @throws UnsupportedEncodingException
     * @throws DuplicatedSortEntries
     * @throws IllegalCriteriaStringException
     */
    private static void setAttributesFromPage(final HttpServletRequest request, final Page page,
            String search, String message, String sortParameters)
            throws UnsupportedEncodingException, DuplicatedSortEntries, IllegalCriteriaStringException {
        if (message != null) {
            request.setAttribute("message", message);
        }
        if (search != null) {
            String urlSearch = URLEncoder.encode(search, "utf-8");
            request.setAttribute("urlSearch", urlSearch);
        }
        List<ComputerDTO> computerList = getComputerListFromParameters(page, search, sortParameters);
        List<Integer> pagesToShow = getPagesToShow(page);

        request.setAttribute("search", search);
        request.setAttribute("page", page);
        request.setAttribute("computerList", computerList);
        request.setAttribute("pageList", pagesToShow);
    }

    public static List<Integer> getPagesToShow(Page page) {
        List<Integer> pagesToShow = new ArrayList<>();
        int firstPageToShow = Math.max(0, page.getPageNumber() - 2);
        int nbPages = page.getNbOfPages();
        for (int i = 0; (i < 5) && ((firstPageToShow + i) < nbPages); ++i) {
            pagesToShow.add(firstPageToShow + i);
        }
        return pagesToShow;
    }

    /**
     * Envoi d'une page représentant un certain nombre d'instances de Computer
     *
     * @param request la requête, qui peut contenir les params suivants :
     *        "pageNumber" représentant le numéro de page courante (Première page par
     *        défaut) "pageLength" Représentant le nombre d'éléments par page.
     * @throws ServletException
     */
    @Override // refacto
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        try {
            String search = request.getParameter("search");
            String message = request.getParameter("message");
            String sortParameters = request.getParameter("sort"); // TODO : vérifier que c'est le bon
                                                                  // nom
            // de param
            Page page = getPageFromRequest(request, search);
            setAttributesFromPage(request, page, search, message, sortParameters);
            RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/views/dashboard.jsp");
            rd.forward(request, response);
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("", e);
            throw new ServletException(e);
        } catch (NumberFormatException e) {
            LOG.debug("", e);
            RequestDispatcher rd = request.getRequestDispatcher("/400");
            request.setAttribute("errorCause", "the page number or page length parameter is invalid");
            try {
                rd.forward(request, response);
            } catch (IOException e1) {
                throw new ServletException(e1);
            }
        } catch (DuplicatedSortEntries e) {
            LOG.debug("", e);
            RequestDispatcher rd = request.getRequestDispatcher("/400");
            request.setAttribute("errorCause", "ther is several times the same ");
            try {
                rd.forward(request, response);
            } catch (IOException e1) {
                throw new ServletException(e1);
            }
        } catch (IllegalCriteriaStringException e) {
            LOG.debug("", e);
            RequestDispatcher rd = request.getRequestDispatcher("/400");
            request.setAttribute("errorCause", "the column sorted parameter is invalid");
            try {
                rd.forward(request, response);
            } catch (IOException e1) {
                throw new ServletException(e1);
            }
        }
    }

    // REFACTO
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] selectedComputerIds = request.getParameter("selection").split(",");
        long[] identifiers = new long[selectedComputerIds.length];
        try {
            for (int i = 0; i < selectedComputerIds.length; ++i) {
                identifiers[i] = Long.parseLong(selectedComputerIds[i].trim());
            }
        } catch (NumberFormatException e) {
            LOG.debug("Erreur lors de la récupération des paramètres de suppression d'un ordi", e);
            String errorCause = "un des paramètres n'était pas un nombre correct";
            RequestDispatcher rd = request.getRequestDispatcher("/400");
            request.setAttribute("errorCause", errorCause);
            rd.forward(request, response);
            return;
        }
        String message, urlParameterMessage;
        List<Long> notDeletedId = new ArrayList<>();
        for (long id : identifiers) {
            int res = validator.delete(id);
            if (res == 0) {
                LOG.info("Deletion : ID not found ( " + id + ")");
                notDeletedId.add(id);
            } else if (res == -1) {
                LOG.error("-1 retourné lors du deleteComputer => problème lors de l'exécution de la requête");
                notDeletedId.add(id);
            }
        }
        if (notDeletedId.isEmpty()) {
            message = "Les ordinateurs ont été supprimés de la base";
        } else {
            message = "Les ordinateurs avec les identifiants suivants dans la base n'ont pas été supprimés : ";
            for (Long i : notDeletedId) {
                message += " " + i;
            }
        }

        urlParameterMessage = URLEncoder.encode(message, CHARSET);
        String url = getServletContext().getContextPath() + "/page?message=" + urlParameterMessage;
        response.sendRedirect(url);
    }
}
