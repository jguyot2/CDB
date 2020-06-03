package com.excilys.mapper;

import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.model.Company;
import com.excilys.model.Computer;
import com.excilys.model.ComputerDTO;

/**
 *
 * @author jguyot2
 */
public final class ComputerMapper {
    /**
     */
    private static final Logger LOG = LoggerFactory.getLogger(ComputerMapper.class);

    public static Optional<Computer> computerDTOToComputer(final ComputerDTO dtoComputer) {
        Company company =
            CompanyMapper.companyDTOToCompany(dtoComputer.getStrEntrepriseId()).orElse(null);
        return computerDTOToComputer(dtoComputer, company);
    }

    /**
     *
     * @param dtoComputer un DTO représentant un ordinateur.
     * @param company Le fabricant de l'ordinateur
     * @return l'ordinateur associé.
     */
    public static Optional<Computer> computerDTOToComputer(final ComputerDTO dtoComputer,
        final Company company) {

        if (dtoComputer == null) {
            LOG.info("DTO > computer : param nul");
            return Optional.empty();
        }

        long id = 0;
        try {
            id = Long.parseLong(dtoComputer.getStrId());
        } catch (NumberFormatException e) {
            LOG.debug("DTO > computer : id du dto incohérent");
            return Optional.empty();
        }

        String computerName = null;
        if (dtoComputer.getName() != null && !dtoComputer.getName().isEmpty()) {
            computerName = dtoComputer.getName();
            LOG.info("Nom du PC: " + computerName);
        }

        Optional<LocalDate> intro = DateMapper.stringToLocalDate(dtoComputer.getIntroductionDate());
        Optional<LocalDate> discontinuation =
            DateMapper.stringToLocalDate(dtoComputer.getDiscontinuationDate());

        return Optional.of(
            new Computer(computerName,
                company,
                intro.orElse(null),
                discontinuation.orElse(null),
                id));
    }

    /**
     *
     * @param c un ordinateur, non nul
     * @return conversion d'une instance de Computer vers le DTO associé.
     */
    public static Optional<ComputerDTO> computerToDTO(final Computer c) {
        if (c == null) {
            LOG.info("computer > DTO : param nul");
            return Optional.empty();
        }
        String name = c.getName();
        String id = c.getId() == 0 ? null : String.valueOf(c.getId());
        Optional<String> dateIntro = DateMapper.localDateToString(c.getIntroduction());
        Optional<String> dateDisco = DateMapper.localDateToString(c.getDiscontinuation());

        return Optional.of(
            new ComputerDTO(
                name,
                id,
                null,
                dateIntro.orElse(null),
                dateDisco.orElse(null)));
    }

    private ComputerMapper() {
    }
}
