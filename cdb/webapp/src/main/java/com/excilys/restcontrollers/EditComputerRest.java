package com.excilys.restcontrollers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.adapters.CompanyAdapter;
import com.excilys.adapters.ComputerAdapter;
import com.excilys.adapters.ComputerDTOProblems;
import com.excilys.adapters.InvalidComputerDtoException;
import com.excilys.model.ComputerDto;
import com.excilys.service.ComputerProblems;
import com.excilys.service.InvalidComputerException;

@RestController
@RequestMapping("/api")
public class EditComputerRest {
    Logger LOG = LoggerFactory.getLogger(EditComputerRest.class);

    ComputerAdapter computerAdapter = new ComputerAdapter();
    CompanyAdapter companyAdapter = new CompanyAdapter();

    @PutMapping(path = "/editComputer", produces = "application/json")
    private ResponseEntity<String> editcomputer(@RequestParam(name = "computerName") final String computerName,
            @RequestParam(name = "id") final Long computerId,
            @RequestParam(name = "introduced") final String introduced,
            @RequestParam(name = "discontinued") final String discontinued,
            @RequestParam(name = "companyId") final Long companyId, @NonNull final Model m) {

        this.LOG.trace("Computer edition");
        try {
            ComputerDto c = new ComputerDto(computerName, computerId.toString(), null, introduced,
                    discontinued);
            c.setCompany(this.companyAdapter.findById(companyId).orElse(null));
            int isComputerEdited = this.computerAdapter.updateComputer(c);
            if (isComputerEdited == 0) {
                return ResponseEntity.badRequest().body("{error: \"The computer was not updated\"}");
            } else if (isComputerEdited == -1) {
                this.LOG.info("erreur lors de la modif d'un ordinateur");
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
            this.LOG.info("Instance de DTO pas valide : " + sb.toString());
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
}
