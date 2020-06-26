package com.excilys.servlets;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServlet;

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

import com.excilys.model.CompanyDTO;
import com.excilys.model.ComputerDTO;
import com.excilys.service.CompanyDTOValidator;
import com.excilys.service.ComputerDTOProblems;
import com.excilys.service.ComputerDTOValidator;
import com.excilys.service.ComputerInstanceProblems;
import com.excilys.service.InvalidComputerDTOException;
import com.excilys.service.InvalidComputerInstanceException;

/**
 * Servlet affichant la page qui permet de modifier un ordinateur de la base
 * (GET) ou effectuant la modification (POST).
 *
 * @author jguyot2
 *
 */
@Controller
@RequestMapping("/editComputer")
public class EditComputerServlet extends HttpServlet {
    /** */
    private static final Logger LOG = LoggerFactory.getLogger(EditComputerServlet.class);

    @Autowired
    private CompanyDTOValidator companyValidator;
    @Autowired
    private ComputerDTOValidator computerValidator;

    private static final long serialVersionUID = 1L;

    private Optional<CompanyDTO> getCompanyDtoFromId(@NonNull final Long companyId) {
        if (companyId == 0) {
            return Optional.empty();
        } else {
            // TODO : lancer une exception si company inexistante.
            return this.companyValidator.findById(companyId);

        }
    }

    @PostMapping
    private String editcomputer(@RequestParam(name = "computerName") final String computerName,
            @RequestParam(name = "id") final Long computerId,
            @RequestParam(name = "introduced") final String introduced,
            @RequestParam(name = "discontinued") final String discontinued,
            @RequestParam(name = "companyId") final Long companyId, final Model m) {
        try {
            ComputerDTO c = new ComputerDTO(computerName, computerId.toString(), null, introduced,
                    discontinued);
            c.setCompany(getCompanyDtoFromId(companyId).orElse(null));
            int isComputerEdited = this.computerValidator.updateComputer(c);
            if (isComputerEdited == 0) {
                // TODO : le cas -1
                m.addAttribute("errorCause", "The computer was not updated");
                return "forward:/400";
            } else {
                m.addAttribute("message", "L'ordinateur a bien été mis à jour");
                return "redirect:/page";
            }
        } catch (InvalidComputerDTOException e) {
            List<ComputerDTOProblems> problems = e.getProblems();
            StringBuilder sb = new StringBuilder();
            for (ComputerDTOProblems problem : problems) {
                sb.append(problem.getExplanation() + " <br />\n");
            }
            m.addAttribute("errorCause", sb.toString());
            return "forward:/400";
        } catch (InvalidComputerInstanceException e) {
            List<ComputerInstanceProblems> problems = e.getProblems();
            StringBuilder sb = new StringBuilder();
            for (ComputerInstanceProblems problem : problems) {
                sb.append(problem.getExplanation() + " <br />\n");
            }
            m.addAttribute("errorCause", sb.toString());
            return "forward:/400";
        }
    }

    @GetMapping
    private String getToEditPage(@RequestParam(name = "id") final Long computerId, final Model m) {
        List<CompanyDTO> companyList = this.companyValidator.fetchList();
        Optional<ComputerDTO> foundComputerOpt = this.computerValidator.findById(computerId);
        if (foundComputerOpt.isPresent()) {
            ComputerDTO computer = foundComputerOpt.get();
            if (computer.getCompany() != null) {
                companyList.remove(computer.getCompany());
            }
            m.addAttribute("computer", foundComputerOpt.get());
            m.addAttribute("companyList", companyList);
            return "editComputer";
        } else {
            m.addAttribute("errorCause", "The computer was not found");
            return "forward:/400";
        }
    }

}
