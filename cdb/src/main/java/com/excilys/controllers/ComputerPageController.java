package com.excilys.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.adapters.ComputerAdapter;
import com.excilys.model.ComputerDTO;
import com.excilys.model.Page;
import com.excilys.model.sort.DuplicatedSortEntriesException;
import com.excilys.model.sort.IllegalCriterionStringException;
import com.excilys.model.sort.SortEntry;

@Controller
public class ComputerPageController {

    private static final String CHARSET = "utf-8";

    private static Logger LOG = LoggerFactory.getLogger(ComputerPageController.class);

    /**
     * Récupération de la liste des numéros de pages à afficher dans le bas de la
     * page
     *
     * @param page
     * @return
     */
    private static List<Integer> getPagesToShow(@NonNull final Page page) {
        List<Integer> pagesToShow = new ArrayList<>();
        int firstPageToShow = Math.max(0, page.getPageNumber() - 2);
        int nbPages = page.getNbOfPages();
        for (int i = 0; i < 5 && firstPageToShow + i < nbPages; ++i) {
            pagesToShow.add(firstPageToShow + i);
        }
        return pagesToShow;
    }

    /**
     * «Encodage» de la chaîne de caractères représentant la recherche pour pouvoir
     * être inséree dans l'url.
     *
     * @param search la recherche à encoder
     * @return Optional.empty si la chaîne de recherche
     */
    private static final Optional<String> getSearchUrl(@Nullable final String search) {
        if (search == null || search.isEmpty()) {
            return Optional.empty();
        } else {
            try {
                return Optional.of(URLEncoder.encode(search, CHARSET));
            } catch (UnsupportedEncodingException e) {
                return Optional.of(URLEncoder.encode(search));
            }
        }
    }

    /**
     * Récupération de la liste des entrées de tri correspondant au paramètre "sort"
     * de la requête.
     *
     * @param sortParam
     * @return
     * @throws IllegalCriterionStringException
     */
    private static List<SortEntry> getSortEntryFromParameter(@Nullable final String sortParam)
            throws IllegalCriterionStringException {
        LOG.trace("Récup des sortEntry");
        if (sortParam == null || sortParam.trim().isEmpty()) {
            LOG.trace("pas de param de recherche");
            return new ArrayList<>();
        }
        LOG.trace("un ou plusieurs params de recherche");
        List<SortEntry> ret = new ArrayList<>();
        String[] sortReprs = sortParam.split(",");
        for (String sortParameter : sortReprs) {
            SortEntry se = SortEntry.fromString(sortParameter);
            ret.add(se);
        }
        LOG.trace("retour : " + ret.toString());
        return ret;
    }

    /**
     * Récupération de toutes les entrées de tri à partir des paramètres de tri.
     *
     * @param sortParam        les «anciens» paramètres de tri = une liste d'entrées
     *                         de tri, séparés par une virgule
     * @param newSortParameter le nouveau paramètre de tri
     * @return
     * @throws IllegalCriterionStringException Si une des chaînes est mal formée
     */
    private static List<SortEntry> getSortEntryFromParameters(@Nullable final String sortParam,
            @Nullable final String newSortParameter) throws IllegalCriterionStringException {
        List<SortEntry> entries = getSortEntryFromParameter(sortParam);

        if (newSortParameter != null && !newSortParameter.trim().isEmpty()) {
            boolean[] addNewParameter = { true };
            LOG.trace("Récup des params de tri : nouveau param");
            SortEntry se = SortEntry.fromString(newSortParameter);
            entries = entries.stream().filter(secondSortentry -> {
                if (secondSortentry.equals(se)) {
                    addNewParameter[0] = false;
                    return false;
                }
                return !SortEntry.haveSameCriterion(secondSortentry, se);
            }).collect(Collectors.toList());
            if (addNewParameter[0]) {
                entries.add(se);
            }
            LOG.trace("found params: " + entries.toString());
        }
        return entries;
    }

    /**
     * Construction du paramètre de l'url qui correspondra au paramètre de tri, pour
     * ajouter dans le lien de la page jsp.
     *
     * @param sortEntries la liste des attributs à trier et ler sens
     * @return un optional contenant la valeur du paramètre de tri dans le lien url
     */
    private static Optional<String> getUrlParameterFromSortEntries(
            @Nullable final List<SortEntry> sortEntries) {
        if (sortEntries == null || sortEntries.isEmpty()) {
            return Optional.empty();
        } else {
            StringBuilder searchUrlBuilder = new StringBuilder();
            for (int i = 0; i < sortEntries.size(); ++i) {
                searchUrlBuilder.append(sortEntries.get(i).toString());
                if (i < sortEntries.size() - 1) {
                    searchUrlBuilder.append(",");
                }
            }
            LOG.info("sort param: " + searchUrlBuilder.toString());
            return Optional.of(searchUrlBuilder.toString());
        }
    }

    /**
     * Ajout des attributs qui seront transmis à la page jsp au modèle.
     *
     * @param page
     * @param computerList
     * @param search
     * @param message
     * @param sortUrlParameterValue
     * @param urlSearch
     * @param pagesToShow
     * @param m
     */
    private static void setModelParameters(@NonNull final Page page,
            @NonNull final List<ComputerDTO> computerList, @Nullable final String search,
            @Nullable final String message, @Nullable final String sortUrlParameterValue,
            @Nullable final String urlSearch, @NonNull final List<Integer> pagesToShow,
            @NonNull final Model m) {
        m.addAttribute("page", page);
        m.addAttribute("computerList", computerList);
        m.addAttribute("search", search);
        m.addAttribute("message", message);
        m.addAttribute("sortParameterValue", sortUrlParameterValue);
        m.addAttribute("urlSearch", urlSearch);
        m.addAttribute("pageList", pagesToShow);
    }

    @Autowired
    private ComputerAdapter validator;

    /**
     * Récupération de la liste des ordinateurs correspondant aux paramètres.
     *
     * @param page        la page de recherche, non nul
     * @param search      la recherche effectuée (peut être nul)
     * @param sortEntries les critère d'ordonnancement
     * @return la liste des ordinateurs à afficher dans la page
     * @throws DuplicatedSortEntriesException s'il y a plusieurs requêtes pour
     *                               l'ordonnancement des résultats qui sont
     *                               associés à un même paramètre (e.g deux ordres
     *                               sur le nom).
     */
    private List<ComputerDTO> getComputerList(final Page page, final String search,
            final List<SortEntry> sortEntries) throws DuplicatedSortEntriesException {
        if (search != null) {
            return this.validator.fetchList(page, search, sortEntries);
        } else {
            return this.validator.fetchList(page, sortEntries);
        }
    }

    /**
     * Récupération de la page à partir des paramètres.
     *
     * @param pageLength
     * @param pageNumber
     * @param search
     * @return
     * @throws IllegalArgumentException
     */
    private Page getPageFromParameters(@Nullable final Integer pageLength,
            @NonNull final Integer pageNumber, @Nullable final String search)
            throws IllegalArgumentException {
        Page p = new Page();
        if (search == null) {
            p.setTotalNumberOfElements(this.validator.getNumberOfElements());
        } else {
            p.setTotalNumberOfElements(this.validator.getNumberOfFoundElements(search));
        }
        if (pageLength != null) {
            if (pageLength <= 0) {
                throw new IllegalArgumentException("pageLength is invalid");
            } else {
                p.setPageLength(pageLength);
            }
        }
        if (pageNumber >= 0) {
            p.setPageNumber(pageNumber);
        } else {
            throw new IllegalArgumentException();
        }
        return p;
    }

    /**
     * Affichage de la page
     *
     * @param pageLength
     * @param pageNumber
     * @param search
     * @param sortParam
     * @param newSortParameter
     * @param message
     * @param model
     * @return
     */ // XXX : Création d'un param et d'une classe "pageDTO" pour éviter d'avoir 10^8
        // paramètres
    @GetMapping("/page")
    public String showPage(
            @RequestParam(required = false, name = "pageLength") final Integer pageLength,
            @RequestParam(defaultValue = "0", name = "pageNumber") final Integer pageNumber,
            @RequestParam(required = false, name = "search") final String search,
            @RequestParam(required = false, name = "sort") final String sortParam,
            @RequestParam(required = false, name = "newSortParam") final String newSortParameter,
            @RequestParam(required = false, name = "message") final String message,
            final Model model) {
        try {
            Page p = getPageFromParameters(pageLength, pageNumber, search);
            List<SortEntry> sortEntries = getSortEntryFromParameters(sortParam, newSortParameter);
            List<ComputerDTO> computerList = getComputerList(p, search, sortEntries);
            List<Integer> pagesToShow = getPagesToShow(p);
            Optional<String> sortUrl = getUrlParameterFromSortEntries(sortEntries);
            Optional<String> searchUrl = getSearchUrl(search);

            setModelParameters(p, computerList, search, message, sortUrl.orElse(null),
                    searchUrl.orElse(null), pagesToShow, model);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorCause", "The page number or page length parameter is invalid");
            return "400";
        } catch (IllegalCriterionStringException e) {
            model.addAttribute("errorCause", "The sort criterion string is invalid");
            return "400";
        } catch (DuplicatedSortEntriesException e) {
            model.addAttribute("errorCause", "There is several sort parameters at the same time");
            return "400";
        }
        return "dashboard";
    }
}
