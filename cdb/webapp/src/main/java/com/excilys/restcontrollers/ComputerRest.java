





package com.excilys.restcontrollers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@CrossOrigin("*")
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

     private static class DashboardParameters {

          String search;
          String sort;


          @Override
          public String toString() {
               return "search:" + this.search + "\tsort:" + this.sort;
          }
     }


     private static final Logger LOG = LoggerFactory.getLogger(ComputerRest.class);


     /**
      * Récupération de la liste des entrées de tri correspondant au paramètre "sort" de la requête.
      *
      * @param sortParam
      *
      * @return
      *
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
      * @param sortParam        les «anciens» paramètres de tri = une liste d'entrées de tri, séparés par une
      *                         virgule
      * @param newSortParameter le nouveau paramètre de tri
      *
      * @return
      *
      * @throws IllegalCriterionStringException Si une des chaînes est mal formée
      */
     private static List<SortEntry> getSortEntryFromParameters(@Nullable final String sortParam)
               throws IllegalCriterionStringException {
          return ComputerRest.getSortEntryFromParameter(sortParam); // TODO refacto
     }


     @Autowired
     ComputerAdapter computerValidator;


     @PostMapping(produces = "application/json")
     public ResponseEntity<String> addComputerToDatabase(@RequestBody final ComputerDto c) {
          try {

               long newIdentifier = this.computerValidator.addComputerDTO(c);
               return ResponseEntity.ok("{id:" + newIdentifier + "}");
          } catch (InvalidComputerDtoException | InvalidComputerException e) {
               // TODO
               return ResponseEntity.badRequest().body("{}");
          }
     }

     @DeleteMapping(value = "/{id}", produces = "application/json")
     public ResponseEntity<String> deleteComputer(@PathVariable final Long id) {
          System.out.println("deletion");
          int nbComputersDeleted = this.computerValidator.delete(id);
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
      *
      * @return la liste des ordinateurs à afficher dans la page
      *
      * @throws DuplicatedSortEntriesException s'il y a plusieurs requêtes pour l'ordonnancement des résultats
      *                                        qui sont associés à un même paramètre (e.g deux ordres sur le
      *                                        nom).
      */
     private List<ComputerDto> getComputerList(final Page page, final String search,
               final List<SortEntry> sortEntries)
               throws DuplicatedSortEntriesException {
          if (search != null) {
               return this.computerValidator.fetchList(page, search, sortEntries);
          } else {
               return this.computerValidator.fetchList(page, sortEntries);
          }
     }

     public void setComputerValidator(final ComputerAdapter dtv) {
          this.computerValidator = dtv;
     }

     @GetMapping(produces = "application/json")
     public ResponseEntity<String> showPage(final Page page,
               @RequestParam(required = false) final String search,
               @RequestParam(required = false) final String sort) {
          try {
               DashboardParameters params = new DashboardParameters();
               params.search = search;
               params.sort = sort;

               Page p = page;
               LOG.trace("received list requset");
               LOG.debug("request params = ", String.valueOf(params));
               System.out.println(params);
               List<SortEntry> sortEntries;
               sortEntries = ComputerRest.getSortEntryFromParameters(params.sort);
               List<ComputerDto> computerList = getComputerList(p, params.search, sortEntries);
               String JsonAnswer = ComputerToJsonMapper.fromComputerList(computerList);
               return ResponseEntity.ok(JsonAnswer);
          } catch (IllegalCriterionStringException e) {
               return (ResponseEntity<String>) ResponseEntity.badRequest();
          } catch (DuplicatedSortEntriesException e) {
               return (ResponseEntity<String>) ResponseEntity.badRequest();
          }
     }

     // TODO rendre ça plus propre
     @GetMapping("/count")
     public ResponseEntity<String> computerCount(@RequestParam(required = false) final String search) {
          if (search == null || search.trim().isEmpty()) {
               return ResponseEntity.ok(this.computerValidator.getNumberOfElements() + "");
          } else {
               return ResponseEntity.ok("" + this.computerValidator.getNumberOfFoundElements(search));
          }
     }
}
