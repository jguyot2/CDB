package com.excilys.servlets;

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

import com.excilys.model.CompanyDTO;
import com.excilys.model.ComputerDTO;
import com.excilys.service.CompanyDTOValidator;
import com.excilys.service.ComputerDTOProblems;
import com.excilys.service.ComputerDTOValidator;
import com.excilys.service.ComputerInstanceProblems;
import com.excilys.service.InvalidComputerDTOException;
import com.excilys.service.InvalidComputerInstanceException;

@Controller
@RequestMapping("addComputer")
public class AddComputerServlet {

    @Autowired
    private static CompanyDTOValidator companyValidator;

    @Autowired
    private static ComputerDTOValidator computerValidator;

    @GetMapping
    public String showAddComputerPage(final Model m) {
        List<CompanyDTO> companyList = AddComputerServlet.companyValidator.fetchList();
        m.addAttribute("companyList", companyList);
        return "addComputer";
    }

    @PostMapping
    public String addComputerToDatabase(
            @RequestParam(name = "computerName") final String computerName,
            @RequestParam(name = "introduced") final String introduced,
            @RequestParam(name = "discontinued") final String discontinued,
            @RequestParam(name = "companyId") final Long companyId, final Model m) {
        try {
            ComputerDTO c = new ComputerDTO(computerName, "0", null, introduced, discontinued);
            c.setCompany(getCompanyDtoFromId(companyId).orElse(null));
            computerValidator.addComputerDTO(c); // TODO vérifier la valeur retournée
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
        m.addAttribute("message", "L'ordinateur a bien été ajouté à la base");
        return "redirect:/page";
    }

    private Optional<CompanyDTO> getCompanyDtoFromId(@NonNull final Long companyId) {
        if (companyId == 0) {
            return Optional.empty();
        } else {
            // TODO : lancer une exception si company inexistante.
            return companyValidator.findById(companyId);

        }
    }

    /** */
    private static final Logger LOG = LoggerFactory.getLogger(AddComputerServlet.class);

    public static void setCompanyValidator(final CompanyDTOValidator dtv) {
        companyValidator = dtv;
    }

    public static void setComputerValidator(final ComputerDTOValidator dtv) {
        computerValidator = dtv;
    }

}
