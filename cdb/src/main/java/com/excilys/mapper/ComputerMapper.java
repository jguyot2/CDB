package com.excilys.mapper;

import java.time.LocalDate;
import java.util.Optional;

import com.excilys.model.Company;
import com.excilys.model.Computer;
import com.excilys.model.ComputerDTO;


/**
 *
 * @author jguyot2
 */
public final class ComputerMapper {
    /**
     *
     * @param dtoComputer un DTO représentant un ordinateur.
     * @return l'ordinateur associé.
     */
    public static Optional<Computer> computerDTOToComputer(final ComputerDTO dtoComputer,
        final Company company) {

        if (dtoComputer == null) {
            return Optional.empty();
        }

        long id = 0;
        try {
            id = Long.parseLong(dtoComputer.getStrId());
        } catch (NumberFormatException e) {
        }

        String computerName = null;
        if (dtoComputer.getName() != null && !dtoComputer.getName().isEmpty()) {
            computerName = dtoComputer.getName();
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
            return Optional.empty();
        }
        String name = c.getName();
        String id = c.getId() == 0 ? String.valueOf(c.getId()) : "";
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
