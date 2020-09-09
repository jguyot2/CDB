package com.excilys.controllers;

import java.util.List;

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


@Controller
@RequestMapping("/addComputer")
public class AddComputerController {

    private static final Logger LOG = LoggerFactory.getLogger(AddComputerController.class);

    @Autowired
    private CompanyAdapter companyValidator;

    @Autowired
    private ComputerAdapter computerValidator;


    /**
     * Ajout d'un ordi dans la base
     *
     * @param computerName
     * @param introduced   Représentant la date de commercialisation, de la forme donnée dans DateMapper
     * @param discontinued Date d'arrêt de production
     * @param companyId
     * @param m
     *
     * @return
     */ // XXX : Refacto pour obtenir directement une instance de ComputerDTO
    @PostMapping
    public String addComputerToDatabase(@RequestParam(name = "computerName") final String computerName,
            @RequestParam(name = "introduced") final String introduced,
            @RequestParam(name = "discontinued") final String discontinued,
            @RequestParam(name = "companyId") final Long companyId, final Model m) {

        AddComputerController.LOG.trace("Ajout d'un ordi à la base");
        try {
            CompanyDto company = companyId == 0 ? null
                    : this.companyValidator.findById(companyId).orElse(null);
            ComputerDto c = new ComputerDto(computerName, 0L, company, introduced, discontinued);
            this.computerValidator.addComputerDTO(c);
        } catch (InvalidComputerDtoException e) {
            AddComputerController.LOG.debug("", e);
            List<ComputerDTOProblems> problems = e.getProblems();
            StringBuilder sb = new StringBuilder();
            for (ComputerDTOProblems problem : problems) {
                sb.append(problem.getExplanation() + " <br />\n");
            }
            AddComputerController.LOG.info("DTO invalide : " + sb.toString());
            m.addAttribute("errorCause", sb.toString());
            return "400";
        } catch (InvalidComputerException e) {
            List<ComputerProblems> problems = e.getProblems();
            StringBuilder sb = new StringBuilder();
            for (ComputerProblems problem : problems) {
                sb.append(problem.getExplanation() + " <br />\n");
            }
            AddComputerController.LOG.info("Ordi invalide : " + sb.toString());
            m.addAttribute("errorCause", sb.toString());
            return "400";
        }
        String urlMessage = UrlEncoding.encode("L'ordinateur a bien été ajouté à la base");
        return "redirect:/page?message=" + urlMessage;
    }

    public void setCompanyValidator(final CompanyAdapter dtv) {
        this.companyValidator = dtv;
    }

    public void setComputerValidator(final ComputerAdapter dtv) {
        this.computerValidator = dtv;
    }

    /**
     * Affichage de la page pour ajouter un ordinateur
     *
     * @param m
     *
     * @return
     */
    @GetMapping
    public String showAddComputerPage(@NonNull final Model m) {
        AddComputerController.LOG.trace("Redirection vers le computerPage");
        List<CompanyDto> companyList = this.companyValidator.fetchList();
        m.addAttribute("companyList", companyList);
        return "addComputer";
    }
}
