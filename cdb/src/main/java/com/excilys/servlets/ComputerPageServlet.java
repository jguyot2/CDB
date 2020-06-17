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
import com.excilys.model.IllegalCriterionStringException;
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

    /**
     * Suppression des ordinateurs dont les identifiants sont dans la base
     *
     * @param computersIdToDelete la liste des identidiants des ordinateurs à
     *        supprimer
     * @return la liste des identifiants qui n'ont pas été supprimées
     */
    private static List<Long> deleteComputers(final List<Long> computersIdToDelete) {
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

    /**
     * Redirection vers la page d'erreur 400.
     *
     * @param request
     * @param response
     * @param errorCause
     * @throws ServletException
     * @throws IOException
     */ // todo : pê la déplacer vers une classe statique
    private static void forwardToError400Page(final HttpServletRequest request,
            final HttpServletResponse response, final String errorCause)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/400");
        request.setAttribute("errorCause", errorCause);
        rd.forward(request, response);
    }

    /**
     * Récupération de la liste des ordinateurs correspondant aux paramètres/
     *
     * @param page la page de recherche, non nul
     * @param search la recherche effectuée (peut être nul)
     * @param sortEntries les critère d'ordonnancement
     * @return la liste des ordinateurs à afficher dans la page
     * @throws DuplicatedSortEntries s'il y a plusieurs requêtes pour
     *         l'ordonnancement des résultats qui sont associés à un même paramètre
     *         (e.g deux ordres sur le nom).
     */
    private static List<ComputerDTO> getComputerList(final Page page, final String search,
            final List<SortEntry> sortEntries) throws DuplicatedSortEntries {
        if (search != null) {
            return validator.fetchList(page, search, sortEntries);
        } else {
            return validator.fetchList(page, sortEntries);
        }
    }

    /**
     * Récupération d'un object page lors de l'affichage de la requête GET pour
     * afficher les ordinateurs
     *
     * @param request la requête, comprenant les paramètres optionnels search
     *        (=chaîne recherchée) pageNumber et pageLength
     * @return la page associée aux paramètres
     * @throws NumberFormatException si pageNumber ou pageLength ne correspondant pas
     *         à des nombres
     */
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

    /**
     * Récupération de la liste des numéros de pages à afficher dans le bas de la
     * page
     *
     * @param page la page courante
     * @return
     */
    private static List<Integer> getPagesToShow(final Page page) {
        List<Integer> pagesToShow = new ArrayList<>();
        int firstPageToShow = Math.max(0, page.getPageNumber() - 2);
        int nbPages = page.getNbOfPages();
        for (int i = 0; (i < 5) && ((firstPageToShow + i) < nbPages); ++i) {
            pagesToShow.add(firstPageToShow + i);
        }
        return pagesToShow;
    }

    /**
     * Récupération de la liste des ordinateurs que l'on cherche à supprimer dans une
     * requête POST
     *
     * @param request la requête POST pour la suppression d'ordinateurs
     * @return la liste des identifiants sélectionnés
     * @throws NumberFormatException
     * @throws IllegalArgumentException
     */
    private static List<Long> getSelectedComputersFromRequest(final HttpServletRequest request)
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

    /**
     * Récupération de toutes les instructions de tri directement à partir de la
     * requête GET
     *
     * @param request
     * @return Une list représentant les instructions de tri s'il y en avait, une
     *         liste vide sinon
     * @throws IllegalCriterionStringException
     */
    private static List<SortEntry> getSortEntriesFromRequest(final HttpServletRequest request)
            throws IllegalCriterionStringException {
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
     * Récupération de la liste des entrées de tri correspondant au paramètre "sort"
     *
     *
     * @param sortParam
     * @return
     * @throws IllegalCriterionStringException
     */
    private static List<SortEntry> getSortEntryFromParameter(final String sortParam)
            throws IllegalCriterionStringException {
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

    /**
     * Construction du paramètre de l'url qui permettra de "garder" le tri dans la
     * page jsp
     *
     * @param sortEntries la liste des attributs à trier et ler sens
     * @return un optional contenant la valeur du paramètre de tri dans le lien url
     *
     */
    private static Optional<String> getUrlParameterFromSortEntries(final List<SortEntry> sortEntries) {
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

    /**
     * Assignation des attributs de la requête qui seront utilisés dans la page jsp.
     *
     * @param request
     * @param page
     * @param computerList
     * @param pagesToShow
     * @param search
     * @param message
     * @param sortUrl
     * @throws UnsupportedEncodingException
     */
    private static void setAttributes(final HttpServletRequest request, final Page page,
            final List<ComputerDTO> computerList, final List<Integer> pagesToShow, final String search,
            final String message, final Optional<String> sortUrl) throws UnsupportedEncodingException {

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

    // TODO javadoc et pê changer le nom de fonction
    private static void setRequestAttributes(final HttpServletRequest request)
            throws UnsupportedEncodingException, DuplicatedSortEntries, IllegalCriterionStringException {

        String search = request.getParameter("search");
        String message = request.getParameter("message");
        Page page = getPageFromRequest(request);
        List<SortEntry> sortEntries = getSortEntriesFromRequest(request);
        Optional<String> sortUrlParameterValue = getUrlParameterFromSortEntries(sortEntries);
        List<ComputerDTO> computerList = getComputerList(page, search, sortEntries);
        List<Integer> pagesToShow = getPagesToShow(page);

        setAttributes(request, page, computerList, pagesToShow, search, message, sortUrlParameterValue);
    }

    /**
     * Envoi d'une page représentant un certain nombre d'instances de Computer
     *
     * @param request la requête, contenant les paramètres suivants <br/>
     *        - pageLength : la taille de la page à afficher. 10 par défaut <br/>
     *        - pageNumber : le numéro de la page à afficher. 0 par défaut <br/>
     *        - search : la recherche effectuée (optionnel) <br/>
     *        - sort : Les instructions de tri. Une instruction de tri correspond à
     *        une chaîne de la forme "[critère de tri]-[sens]". cf SortEntry ou
     *        SortCriterion pour les formes<br/>
     *        - newSortParam : La dernière instruction de tri effectuée. <br/>
     * @param response
     * @throws ServletException
     * @throws IOException
     * @see SortEntry
     */
    @Override
    public void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // TODO : redirection si les paramètres de page sont incohérents
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
        } catch (IllegalCriterionStringException e) {
            LOG.debug("", e);
            forwardToError400Page(request, response, "The sort parameter is invalid");
            return;
        }
    }

    /**
     * Suppression d'un certain nombre d'ordinateurs sélectionnés.
     *
     * @param request la requête avec un paramètre "selection" indiquant les
     *        identifiants des ordinateurs à supprimer
     * @param response
     *
     */ // REFACTO Séparation en deux sous-fonctions (pê)
    @Override
    public void doPost(final HttpServletRequest request, final HttpServletResponse response)
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

    /**
     * Récupération du message indiquant si tous les ordinateurs ont été (ou non)
     * supprimés
     *
     * @param notDeletedId la liste des ordinateurs pas supprimés
     * @return la chaîne de caractères qui indique quels identifiants n'ont pas été
     *         supprimés
     */
    public String getMessageFromNotDeletedId(final List<Long> notDeletedId) {
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
}
