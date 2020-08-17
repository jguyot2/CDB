package com.excilys.restcontrollers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.adapters.CompanyAdapter;
import com.excilys.adapters.ComputerAdapter;
import com.excilys.adapters.ComputerDTOProblems;
import com.excilys.adapters.InvalidComputerDtoException;
import com.excilys.model.ComputerDto;
import com.excilys.model.Page;
import com.excilys.model.sort.DuplicatedSortEntriesException;
import com.excilys.model.sort.IllegalCriterionStringException;
import com.excilys.model.sort.SortEntry;
import com.excilys.service.ComputerProblems;
import com.excilys.service.InvalidComputerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/computers")
public class ComputerRest {
	private static class ComputerToJsonMapper {
		private static ObjectMapper obj = new ObjectMapper();

		public static String fromComputerList(final List<ComputerDto> list) {
			try {
				return ComputerToJsonMapper.obj.writeValueAsString(list);
			} catch (JsonProcessingException e) {
				return "{}";
			}
		}
	}

	private static final Logger LOG = LoggerFactory.getLogger(ComputerRest.class);

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
	 * @param sortParam        les «anciens» paramètres de tri = une liste d'entrées
	 *                         de tri, séparés par une virgule
	 * @param newSortParameter le nouveau paramètre de tri
	 * @return
	 * @throws IllegalCriterionStringException Si une des chaînes est mal formée
	 */
	private static List<SortEntry> getSortEntryFromParameters(@Nullable final String sortParam,
			@Nullable final String newSortParameter) throws IllegalCriterionStringException {
		List<SortEntry> entries = ComputerRest.getSortEntryFromParameter(sortParam);

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

	@Autowired
	ComputerAdapter computerValidator;

	@Autowired
	private CompanyAdapter companyValidator;

	@PostMapping(produces = "application/json")
	public ResponseEntity<String> addComputerToDatabase(@RequestBody final ComputerDto c, final Model m) {
		try {
			long newIdentifier = this.computerValidator.addComputerDTO(c);
			return ResponseEntity.ok("{id:" + newIdentifier + "}");
		} catch (InvalidComputerDtoException | InvalidComputerException e) {
			// TODO
			return ResponseEntity.badRequest().body("{}");
		}
	}

	@DeleteMapping(produces = "application/json")
	public ResponseEntity<String> deleteComputer(final Long[] identifiers) {
		System.out.println("deletion");
		int nbComputersDeleted = this.computerValidator.delete(identifiers);
		return ResponseEntity.ok("{}");
	}

	@PutMapping(produces = "application/json") // Refacto
	private ResponseEntity<String> editcomputer(@RequestBody final ComputerDto c, @NonNull final Model m) {
		ComputerRest.LOG.trace("Computer edition");
		try {
			int isComputerEdited = this.computerValidator.updateComputer(c);
			if (isComputerEdited == 0) {
				return ResponseEntity.badRequest().body("{error: \"The computer was not updated\"}");
			} else if (isComputerEdited == -1) {
				ComputerRest.LOG.info("erreur lors de la modif d'un ordinateur");
				return ResponseEntity.status(500).body("{error : \"internal error\"}");
			} else {
				return ResponseEntity.ok("{}");
			}
		} catch (InvalidComputerDtoException e) {
			List<ComputerDTOProblems> problems = e.getProblems();
			StringBuilder sb = new StringBuilder();
			for (ComputerDTOProblems problem : problems) {
				sb.append(problem.getExplanation() + " <br />\n");
			}
			ComputerRest.LOG.info("Instance de DTO pas valide : " + sb.toString());
			return ResponseEntity.badRequest().body("{error : \"" + sb.toString() + "\"}");

		} catch (InvalidComputerException e) {
			List<ComputerProblems> problems = e.getProblems();
			StringBuilder sb = new StringBuilder();
			for (ComputerProblems problem : problems) {
				sb.append(problem.getExplanation() + " <br />\n");
			}
			return ResponseEntity.badRequest().body("{error : \"" + sb.toString() + "\"}");
		}
	}

	/**
	 * Récupération de la liste des ordinateurs correspondant aux paramètres.
	 *
	 * @param page        la page de recherche, non nul
	 * @param search      la recherche effectuée (peut être nul)
	 * @param sortEntries les critère d'ordonnancement
	 * @return la liste des ordinateurs à afficher dans la page
	 * @throws DuplicatedSortEntriesException s'il y a plusieurs requêtes pour
	 *                                        l'ordonnancement des résultats qui
	 *                                        sont associés à un même paramètre (e.g
	 *                                        deux ordres sur le nom).
	 */
	private List<ComputerDto> getComputerList(final Page page, final String search, final List<SortEntry> sortEntries)
			throws DuplicatedSortEntriesException {
		if (search != null) {
			return this.computerValidator.fetchList(page, search, sortEntries);
		} else {
			return this.computerValidator.fetchList(page, sortEntries);
		}
	}

	public void setCompanyValidator(final CompanyAdapter dtv) {
		this.companyValidator = dtv;
	}

	public void setComputerValidator(final ComputerAdapter dtv) {
		this.computerValidator = dtv;
	}

	private static class DashboardParameters {
		Integer pageLength;
		Integer pageNumber;
		String search;
		String sort;
		String newSortParam;
		String message;
	}

	private Page getPageFromDashboardParams(final DashboardParameters params) {
		Page p = new Page();
		if (params.pageLength != null) {
			p.setPageLength(params.pageLength);
		} // TODO : vérification de la cohérence des paramètres + recherche + nombre total
			// d'éléments
		p.setPageNumber(params.pageNumber);
		return p;
	}

	@GetMapping(produces = "application/json")
	public ResponseEntity<String> showPage(@RequestAttribute final DashboardParameters params, final Model model) {
		try {
			Page p = getPageFromDashboardParams(params);
			List<SortEntry> sortEntries;
			sortEntries = ComputerRest.getSortEntryFromParameters(params.sort, params.newSortParam);
			List<ComputerDto> computerList = getComputerList(p, params.search, sortEntries);
			String JsonAnswer = ComputerToJsonMapper.fromComputerList(computerList);
			return ResponseEntity.ok(JsonAnswer);
		} catch (IllegalCriterionStringException e) {
			return (ResponseEntity<String>) ResponseEntity.badRequest();
		} catch (DuplicatedSortEntriesException e) {
			return (ResponseEntity<String>) ResponseEntity.badRequest();
		}
	}

}
