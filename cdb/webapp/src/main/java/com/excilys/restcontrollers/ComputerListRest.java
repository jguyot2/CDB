package com.excilys.restcontrollers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.adapters.ComputerAdapter;
import com.excilys.model.ComputerDto;
import com.excilys.model.Page;
import com.excilys.model.sort.DuplicatedSortEntriesException;
import com.excilys.model.sort.IllegalCriterionStringException;
import com.excilys.model.sort.SortEntry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
public class ComputerListRest {
    private static class ComputerToJsonMapper {
        private static ObjectMapper obj = new ObjectMapper();

        public static String fromComputerList(final List<ComputerDto> list) {
            try {
                return obj.writeValueAsString(list);
            } catch (JsonProcessingException e) {
                return "{}";
            }
        }
    }

    @Autowired
    ComputerAdapter adapter;

    /**
     * Récupération de la liste des entrées de tri correspondant au paramètre "sort" de la requête.
     *
     * @param sortParam
     * @return
     * @throws IllegalCriterionStringException
     */
    private static List<SortEntry> getSortEntryFromParameter(@Nullable final String sortParam)
            throws IllegalCriterionStringException {
        if (sortParam == null || sortParam.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<SortEntry> ret = new ArrayList<>();
        String[] sortReprs = sortParam.split(",");
        for (String sortParameter : sortReprs) {
            SortEntry se = SortEntry.fromString(sortParameter);
            ret.add(se);
        }
        return ret;
    }

    /**
     * Récupération de toutes les entrées de tri à partir des paramètres de tri.
     *
     * @param sortParam        les «anciens» paramètres de tri = une liste d'entrées de tri, séparés
     *                         par une virgule
     * @param newSortParameter le nouveau paramètre de tri
     * @return
     * @throws IllegalCriterionStringException Si une des chaînes est mal formée
     */
    private static List<SortEntry> getSortEntryFromParameters(@Nullable final String sortParam,
            @Nullable final String newSortParameter) throws IllegalCriterionStringException {
        List<SortEntry> entries = getSortEntryFromParameter(sortParam);

        if (newSortParameter != null && !newSortParameter.trim().isEmpty()) {
            boolean[] addNewParameter = { true };
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
        }
        return entries;
    }

    @GetMapping(value = "/list", produces = "application/json")
    public String getList() {
        new ComputerToJsonMapper();
        return ComputerToJsonMapper.fromComputerList(this.adapter.fetchList());
    }

    @GetMapping(value = "/page", produces = "application/json")
    public ResponseEntity<String> showPage(
            @RequestParam(required = false, name = "pageLength") final Integer pageLength,
            @RequestParam(defaultValue = "0", name = "pageNumber") final Integer pageNumber,
            @RequestParam(required = false, name = "search") final String search,
            @RequestParam(required = false, name = "sort") final String sortParam,
            @RequestParam(required = false, name = "newSortParam") final String newSortParameter,
            @RequestParam(required = false, name = "message") final String message, final Model model) {
        try {
            Page p = getPageFromParameters(pageLength, pageNumber, search);
            List<SortEntry> sortEntries;
            sortEntries = getSortEntryFromParameters(sortParam, newSortParameter);
            List<ComputerDto> computerList = getComputerList(p, search, sortEntries);
            String JsonAnswer = ComputerToJsonMapper.fromComputerList(computerList);
            return ResponseEntity.ok(JsonAnswer);
        } catch (IllegalCriterionStringException e) {
            return (ResponseEntity<String>) ResponseEntity.badRequest();

        } catch (DuplicatedSortEntriesException e) {
            return (ResponseEntity<String>) ResponseEntity.badRequest();
        }
    }

    /**
     * Récupération de la liste des ordinateurs correspondant aux paramètres.
     *
     * @param page        la page de recherche, non nul
     * @param search      la recherche effectuée (peut être nul)
     * @param sortEntries les critère d'ordonnancement
     * @return la liste des ordinateurs à afficher dans la page
     * @throws DuplicatedSortEntriesException s'il y a plusieurs requêtes pour l'ordonnancement des
     *                                        résultats qui sont associés à un même paramètre (e.g
     *                                        deux ordres sur le nom).
     */
    private List<ComputerDto> getComputerList(final Page page, final String search, final List<SortEntry> sortEntries)
            throws DuplicatedSortEntriesException {
        if (search != null) {
            return this.adapter.fetchList(page, search, sortEntries);
        } else {
            return this.adapter.fetchList(page, sortEntries);
        }
    }

    private Page getPageFromParameters(@Nullable final Integer pageLength, @NonNull final Integer pageNumber,
            @Nullable final String search) throws IllegalArgumentException {
        Page p = new Page();
        if (search == null) {
            p.setTotalNumberOfElements(this.adapter.getNumberOfElements());
        } else {
            p.setTotalNumberOfElements(this.adapter.getNumberOfFoundElements(search));
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

}
