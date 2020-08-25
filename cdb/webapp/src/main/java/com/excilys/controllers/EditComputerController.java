





package com.excilys.controllers;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.adapters.CompanyAdapter;
import com.excilys.adapters.ComputerAdapter;
import com.excilys.adapters.ComputerDTOProblems;
import com.excilys.adapters.InvalidComputerDtoException;
import com.excilys.model.CompanyDto;
import com.excilys.model.ComputerDto;
import com.excilys.service.ComputerProblems;
import com.excilys.service.InvalidComputerException;


/**
 * Servlet affichant la page qui permet de modifier un ordinateur de la base (GET) ou effectuant la
 * modification (POST).
 *
 * @author jguyot2
 *
 */
@Controller
@RequestMapping("/editComputer")
public class EditComputerController {

     private static final Logger LOG = LoggerFactory.getLogger(EditComputerController.class);

     @Autowired
     private CompanyAdapter companyValidator;

     @Autowired
     private ComputerAdapter computerValidator;


     /**
      * Modification d'un ordinateur dans la base
      *
      * @param computerName
      * @param computerId
      * @param introduced
      * @param discontinued
      * @param companyId
      * @param m
      *
      * @return
      */ // XXX : màj pour prendre un dto en paramètre
     @PostMapping
     private String editcomputer(@RequestParam(name = "computerName") final String computerName,
               @RequestParam(name = "id") final Long computerId,
               @RequestParam(name = "introduced") final String introduced,
               @RequestParam(name = "discontinued") final String discontinued,
               @RequestParam(name = "companyId") final Long companyId, @NonNull final Model m) {
          EditComputerController.LOG.trace("Computer edition");
          try {
               ComputerDto c = new ComputerDto(computerName, computerId, (CompanyDto) null, introduced,
                         discontinued);
               c.setCompany(this.companyValidator.findById(companyId).orElse(null));
               int isComputerEdited = this.computerValidator.updateComputer(c);
               if (isComputerEdited == 0) {
                    m.addAttribute("errorCause", "The computer was not updated");
                    return "400";
               } else if (isComputerEdited == -1) {
                    EditComputerController.LOG.info("erreur lors de la modif d'un ordinateur");
                    m.addAttribute("errorCause", "Internal db bullshit");
                    return "500";
               } else {
                    return "redirect:/page?message="
                              + UrlEncoding.encode("L'ordinateur a bien été mis à jour");
               }
          } catch (InvalidComputerDtoException e) {
               List<ComputerDTOProblems> problems = e.getProblems();
               StringBuilder sb = new StringBuilder();
               for (ComputerDTOProblems problem : problems) {
                    sb.append(problem.getExplanation() + " <br />\n");
               }

               EditComputerController.LOG.info("Instance de DTO pas valide : " + sb.toString());
               m.addAttribute("errorCause", sb.toString());
               return "400";
          } catch (InvalidComputerException e) {
               List<ComputerProblems> problems = e.getProblems();
               StringBuilder sb = new StringBuilder();
               for (ComputerProblems problem : problems) {
                    sb.append(problem.getExplanation() + " <br />\n");
               }

               EditComputerController.LOG.info("Instance de computer pas valide : " + sb.toString());
               m.addAttribute("errorCause", sb.toString());
               return "400";
          }
     }

     /**
      * Affichage de la page de l'ordinateur dont l'identifiant est donné en para
      *
      * @param computerId l'identifiant
      * @param m
      *
      * @return
      */
     @GetMapping
     private String getToEditPage(@RequestParam(name = "id") final Long computerId, @NonNull final Model m) {
          List<CompanyDto> companyList = this.companyValidator.fetchList();
          Optional<ComputerDto> foundComputerOpt = this.computerValidator.findById(computerId);
          if (foundComputerOpt.isPresent()) {
               ComputerDto computer = foundComputerOpt.get();
               if (computer.getCompany() != null) {
                    companyList.remove(computer.getCompany());
               }
               m.addAttribute("computer", foundComputerOpt.get());
               m.addAttribute("companyList", companyList);
               return "editComputer";
          } else {
               m.addAttribute("errorCause", "The computer was not found");
               return "400";
          }
     }
}
