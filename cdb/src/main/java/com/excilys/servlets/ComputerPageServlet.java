package com.excilys.servlets;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

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

    private static List<ComputerDTO> getComputerListFromParameters(final Page page, String search,
            List<SortEntry> sortEntries) throws DuplicatedSortEntries, IllegalCriteriaStringException {
        if (search != null) {
            return validator.fetchList(page, search, sortEntries);
        } else {
            return validator.fetchList(page, sortEntries);
        }
    }

    private static void setAttributesFromPage(final HttpServletRequest request, final Page page,
            String search, String message, List<SortEntry> sortEntries)
            throws UnsupportedEncodingException, DuplicatedSortEntries, IllegalCriteriaStringException {
        if (message != null) {
            request.setAttribute("message", message);
        }
        if (search != null) {
            String urlSearch = URLEncoder.encode(search, "utf-8");
            request.setAttribute("urlSearch", urlSearch);
        }
        if (!sortEntries.isEmpty()) {
            StringBuilder searchUrlBuilder = new StringBuilder();
            for (int i = 0; i < sortEntries.size(); ++i) {
                searchUrlBuilder.append(sortEntries.get(i).toString()); // TODO : pê mettre une fonction
                                                                        // statique ici plutôt
                // qu'utiliser le toString
                if (i < (sortEntries.size() - 1)) {
                    searchUrlBuilder.append(",");
                }
            }
            LOG.info("sort param: " + searchUrlBuilder.toString());
            request.setAttribute("sortParameterValue", searchUrlBuilder.toString());
        }
        List<ComputerDTO> computerList = getComputerListFromParameters(page, search, sortEntries);
        List<Integer> pagesToShow = getPagesToShow(page);

        request.setAttribute("search", search);
        request.setAttribute("page", page);
        request.setAttribute("computerList", computerList);
        request.setAttribute("pageList", pagesToShow);

        LOG.info("parameters put to the requête: ");

        Enumeration<String> e = request.getAttributeNames();
        while (e.hasMoreElements()) {
            String k = e.nextElement();
            LOG.info(k + " : " + request.getAttribute(k));
        }
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

    // Refacto sur les noms choisis avec le cul
    public static List<SortEntry> getSortEntryFromSortParam(String sortParam)
            throws IllegalCriteriaStringException {
        LOG.info("Récup des sortEntry");
        if ((sortParam == null) || sortParam.trim().isEmpty()) {
            LOG.info("pas de param search");
            return new ArrayList<>();
        }
        List<SortEntry> ret = new ArrayList<>();
        String[] sortReprs = sortParam.split(",");
        for (String sortParameter : sortReprs) {
            SortEntry se = SortEntry.fromString(sortParameter);
            ret.add(se);
        }
        LOG.info("reour : " + ret.toString());
        return ret;
    }

    public List<SortEntry> getSortEntryFromSortAndNewParam(String sortParam, String newSortParameter)
            throws IllegalCriteriaStringException {
        LOG.info("Récup des params de tri");
        List<SortEntry> ret = getSortEntryFromSortParam(sortParam);
        if ((newSortParameter == null) || newSortParameter.trim().isEmpty()) {
            return ret;
        } else {
            LOG.info("Récup des params de tri : nouveau param");
            SortEntry se = SortEntry.fromString(newSortParameter);
            ret = ret.stream().filter(secondSortentry -> !SortEntry.haveSameCriterion(secondSortentry, se))
                    .collect(Collectors.toList());
            ret.add(se);
            LOG.info("found params: " + ret.toString());
            return ret;
        }
    }

    /**
     * Envoi d'une page représentant un certain nombre d'instances de Computer
     *
     * @param request la requête, qui peut contenir les params suivants :
     *        "pageNumber" représentant le numéro de page courante (Première page par
     *        défaut) "pageLength" Représentant le nombre d'éléments par page. -
     *        newSortParam, sort
     * @throws ServletException
     */ // TODO màj de la javadoc
    @Override // refacto
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        try {
            String search = request.getParameter("search");
            String message = request.getParameter("message");
            String sort = request.getParameter("sort");
            String newSortParameter = request.getParameter("newSortParam");

            List<SortEntry> sortEntries = getSortEntryFromSortAndNewParam(sort, newSortParameter);
            LOG.debug("foundSortEntries : " + sortEntries);
            Page page = getPageFromRequest(request, search);
            setAttributesFromPage(request, page, search, message, sortEntries);
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
            request.setAttribute("errorCause", "there is several times the same search criterion");
            try {
                rd.forward(request, response);
            } catch (IOException e1) {
                throw new ServletException(e1);
            }
        } catch (IllegalCriteriaStringException e) {
            LOG.debug("", e);
            RequestDispatcher rd = request.getRequestDispatcher("/400");
            request.setAttribute("errorCause", "The search parameter is invalid");
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
