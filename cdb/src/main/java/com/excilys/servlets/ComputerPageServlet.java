package com.excilys.servlets;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

@WebServlet("/page")
public class ComputerPageServlet extends HttpServlet {

    private static final String CHARSET = "UTF-8";
    private static final long serialVersionUID = 1L;
    private static Logger LOG = LoggerFactory.getLogger(ComputerPageServlet.class);
    private static final ComputerDTOValidator validator = new ComputerDTOValidator();

    private static Page getPageFromRequest(final HttpServletRequest request) throws NumberFormatException {
        String search = request.getParameter("search");
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

    private static List<ComputerDTO> getComputerList(final Page page, String search,
            List<SortEntry> sortEntries) throws DuplicatedSortEntries, IllegalCriteriaStringException {
        if (search != null) {
            return validator.fetchList(page, search, sortEntries);
        } else {
            return validator.fetchList(page, sortEntries);
        }
    }

    private static Optional<String> getUrlParameterFromSortEntries(List<SortEntry> sortEntries) {
        if (sortEntries.isEmpty()) {
            return Optional.empty();
        } else {
            StringBuilder searchUrlBuilder = new StringBuilder();
            for (int i = 0; i < sortEntries.size(); ++i) {
                searchUrlBuilder.append(sortEntries.get(i).toString());
                if (i < (sortEntries.size() - 1)) {
                    searchUrlBuilder.append(",");
                }
            }
            LOG.info("sort param: " + searchUrlBuilder.toString());
            return Optional.of(searchUrlBuilder.toString());
        }
    }

    private static void setAttributes(final HttpServletRequest request, Page page,
            List<ComputerDTO> computerList, List<Integer> pagesToShow, String search, String message,
            Optional<String> sortUrl) throws UnsupportedEncodingException {

        if (search != null) {
            String urlSearch = URLEncoder.encode(search, CHARSET);
            request.setAttribute("urlSearch", urlSearch);
        }
        request.setAttribute("message", message);

        request.setAttribute("search", search);
        request.setAttribute("page", page);
        request.setAttribute("computerList", computerList);
        request.setAttribute("pageList", pagesToShow);
        if (sortUrl.isPresent()) {
            request.setAttribute("sortParameterValue", sortUrl.get());
        }
    }

    private static void setRequestAttributes(final HttpServletRequest request)
            throws UnsupportedEncodingException, DuplicatedSortEntries, IllegalCriteriaStringException {

        String search = request.getParameter("search");
        String message = request.getParameter("message");
        Page page = getPageFromRequest(request);
        List<SortEntry> sortEntries = getSortEntriesFromRequest(request);
        Optional<String> sortUrlParameterValue = getUrlParameterFromSortEntries(sortEntries);
        List<ComputerDTO> computerList = getComputerList(page, search, sortEntries);
        List<Integer> pagesToShow = getPagesToShow(page);

        setAttributes(request, page, computerList, pagesToShow, search, message, sortUrlParameterValue);
    }

    private static List<Integer> getPagesToShow(Page page) {
        List<Integer> pagesToShow = new ArrayList<>();
        int firstPageToShow = Math.max(0, page.getPageNumber() - 2);
        int nbPages = page.getNbOfPages();
        for (int i = 0; (i < 5) && ((firstPageToShow + i) < nbPages); ++i) {
            pagesToShow.add(firstPageToShow + i);
        }
        return pagesToShow;
    }

    // Refacto des noms choisis avec le cul
    private static List<SortEntry> getSortEntryFromParameter(String sortParam)
            throws IllegalCriteriaStringException {
        LOG.info("Récup des sortEntry");
        if ((sortParam == null) || sortParam.trim().isEmpty()) {
            LOG.info("pas de param de recherche");
            return new ArrayList<>();
        }
        LOG.info("un ou plusieurs params de recherche");
        List<SortEntry> ret = new ArrayList<>();
        String[] sortReprs = sortParam.split(",");
        for (String sortParameter : sortReprs) {
            SortEntry se = SortEntry.fromString(sortParameter);
            ret.add(se);
        }
        LOG.info("retour : " + ret.toString());
        return ret;
    }

    private static List<SortEntry> getSortEntriesFromRequest(HttpServletRequest request)
            throws IllegalCriteriaStringException {
        String sortParam = request.getParameter("sort");
        String newSortParameter = request.getParameter("newSortParam");
        LOG.info("Récup des params de tri");
        List<SortEntry> ret = getSortEntryFromParameter(sortParam);
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
     * @throws IOException
     */ // TODO màj de la javadoc
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        try {
            setRequestAttributes(request);
            RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/views/dashboard.jsp");
            rd.forward(request, response);
        } catch (NumberFormatException e) {
            LOG.debug("", e);
            forwardToError400Page(request, response, "the page number or page length parameter is invalid");
            return;
        } catch (DuplicatedSortEntries e) {
            LOG.debug("", e);
            forwardToError400Page(request, response, "there is several times the same search criterion");
            return;
        } catch (IllegalCriteriaStringException e) {
            LOG.debug("", e);
            forwardToError400Page(request, response, "The sort parameter is invalid");
            return;
        }
    }

    private static void forwardToError400Page(HttpServletRequest request, HttpServletResponse response,
            String errorCause) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/400");
        request.setAttribute("errorCause", errorCause);
        rd.forward(request, response);
    }

    private List<Long> getSelectedComputersFromRequest(HttpServletRequest request)
            throws NumberFormatException, IllegalArgumentException {
        String selectionParameter = request.getParameter("selection");
        if (selectionParameter == null) {
            throw new IllegalArgumentException();
        }
        String[] selectedComputerIds = request.getParameter("selection").split(",");
        Long[] identifiers = new Long[selectedComputerIds.length];
        for (int i = 0; i < selectedComputerIds.length; ++i) {
            identifiers[i] = Long.parseLong(selectedComputerIds[i].trim());
        }
        return Arrays.asList(identifiers);
    }

    public String getMessageFromNotDeletedId(List<Long> notDeletedId) {
        String message;
        if (notDeletedId.isEmpty()) {
            message = "Les ordinateurs ont été supprimés de la base";
        } else {
            message = "Les ordinateurs avec les identifiants suivants dans la base n'ont pas été supprimés : ";
            for (Long i : notDeletedId) {
                message += " " + i;
            }
        }
        return message;
    }

    // REFACTO Séparation en deux sous-fonctions
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Long> identifiers;
        try {
            identifiers = getSelectedComputersFromRequest(request);
        } catch (NumberFormatException e) {
            LOG.debug("Erreur lors de la récupération des paramètres de suppression d'un ordi", e);
            forwardToError400Page(request, response, "un des paramètres n'était pas un nombre correct");
            return;
        } catch (IllegalArgumentException e) {
            LOG.debug("", e);
            forwardToError400Page(request, response, "pas de paramètre de sélection");
            return;
        }

        List<Long> notDeletedId = deleteComputers(identifiers);
        String message = getMessageFromNotDeletedId(notDeletedId);
        String urlParameterMessage = URLEncoder.encode(message, CHARSET);
        String url = getServletContext().getContextPath() + "/page?message=" + urlParameterMessage;
        response.sendRedirect(url);
    }

    // TODO javadoc
    private static List<Long> deleteComputers(List<Long> computersIdToDelete) {
        List<Long> notDeletedIds = new ArrayList<>();
        for (long id : computersIdToDelete) {
            int res = validator.delete(id);
            if (res == 0) {
                LOG.info("Deletion : ID not found ( " + id + ")");
                notDeletedIds.add(id);
            } else if (res == -1) {
                LOG.error("-1 retourné lors du deleteComputer => problème lors de l'exécution de la requête");
                notDeletedIds.add(id);
            }
        }
        return notDeletedIds;
    }
}
