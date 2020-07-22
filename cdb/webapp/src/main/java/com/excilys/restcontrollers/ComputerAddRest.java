package com.excilys.restcontrollers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.adapters.CompanyAdapter;
import com.excilys.adapters.ComputerAdapter;
import com.excilys.adapters.InvalidComputerDtoException;
import com.excilys.model.CompanyDto;
import com.excilys.model.ComputerDto;
import com.excilys.service.InvalidComputerException;

@RestController
@RequestMapping("/api")
public class ComputerAddRest {
    private static final Logger LOG = LoggerFactory.getLogger(ComputerAddRest.class);
    @Autowired
    private CompanyAdapter companyValidator;

    @Autowired
    private ComputerAdapter computerValidator;

    // XXX : Refacto pour obtenir directement une instance de ComputerDTO
    @PostMapping(path = "/addComputer", produces = "application/json")
    public ResponseEntity<String> addComputerToDatabase(@RequestParam(name = "computerName") final String computerName,
            @RequestParam(name = "introduced") final String introduced,
            @RequestParam(name = "discontinued") final String discontinued,
            @RequestParam(name = "companyId") final Long companyId, final Model m) {

        try {
            CompanyDto company = companyId == 0 ? null : this.companyValidator.findById(companyId).orElse(null);
            ComputerDto c = new ComputerDto(computerName, 0L, company, introduced, discontinued);
            long newIdentifier = this.computerValidator.addComputerDTO(c);
            return ResponseEntity.ok("{id:" + newIdentifier + "}");
        } catch (InvalidComputerDtoException | InvalidComputerException e) {
            // TODO
            return ResponseEntity.badRequest().body("{}");
        }
    }

    public void setCompanyValidator(final CompanyAdapter dtv) {
        this.companyValidator = dtv;
    }

    public void setComputerValidator(final ComputerAdapter dtv) {
        this.computerValidator = dtv;
    }
}
