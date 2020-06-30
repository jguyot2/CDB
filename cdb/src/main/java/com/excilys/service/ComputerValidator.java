package com.excilys.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.excilys.model.Company;
import com.excilys.model.Computer;

@Service
public class ComputerValidator {
    private static final Logger LOG = LoggerFactory.getLogger(ComputerValidator.class);

    @Autowired
    private CompanyService cv;

    public void validate(final Computer instance) throws InvalidComputerException {
        checkComputerValidity(instance);
    }

    private void checkComputerValidity(final Computer computer)
            throws InvalidComputerException {
        if (computer == null) {
            throw new InvalidComputerException(
                    Arrays.asList(ComputerProblems.NULL_COMPUTER));
        }

        List<ComputerProblems> problems = getComputerInstanceProblems(computer);
        if (problems.size() > 0) {
            LOG.debug("Détection d'une instance de Computer invalide : " + computer);
            throw new InvalidComputerException(problems);
        }
    }

    private void checkCompany(final Computer c, final List<ComputerProblems> problems) {
        Company company = c.getManufacturer();
        if (company != null) {
            if (company.getId() <= 0) {
                problems.add(ComputerProblems.INVALID_COMPANY_ID);
            } else if (!this.cv.findById(company.getId()).isPresent()) {
                problems.add(ComputerProblems.INEXISTENT_COMPANY);
            }
        }
    }

    private void checkDates(final Computer computer,
            final List<ComputerProblems> problems) {
        if (computer.getIntroduction() != null) {
            if (computer.getIntroduction().getYear() < 1970
                    || computer.getIntroduction().getYear() > 2037) {
                problems.add(ComputerProblems.OUT_OF_RANGE_INTRO_DATE);
            }
            if (computer.getDiscontinuation() != null
                    && computer.getIntroduction().compareTo(computer.getDiscontinuation()) > 0) {
                if (computer.getDiscontinuation().getYear() < 1970
                        || computer.getDiscontinuation().getYear() > 2037) {
                    problems.add(ComputerProblems.OUT_OF_RANGE_DISCO_DATE);
                }
                problems.add(ComputerProblems.INVALID_DISCONTINUATION_DATE);
            }

        } else if (computer.getDiscontinuation() != null) {
            problems.add(ComputerProblems.NULL_INTRO_WITH_NOT_NULL_DISCONTINUATION);
        }

    }

    /**
     * @param computer l'instance de Computer à tester.
     *
     * @return Une liste contenant la liste des problèmes sur l'instance de Computer
     *         passée en paramètre
     */
    private List<ComputerProblems> getComputerInstanceProblems(
            @NonNull final Computer computer) {
        List<ComputerProblems> problems = new ArrayList<>();
        if (computer.getName() == null || computer.getName().trim().isEmpty()) {
            problems.add(ComputerProblems.INVALID_NAME);
        }
        checkDates(computer, problems);
        checkCompany(computer, problems);
        return problems;
    }
}
