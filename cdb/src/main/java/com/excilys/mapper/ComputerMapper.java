package com.excilys.mapper;

import java.util.Optional;
import com.excilys.model.ComputerDTO;
import com.excilys.model.Computer;
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
    public static Optional<Computer> computerDTOToComputer(final ComputerDTO dtoComputer) {
        if (dtoComputer == null) {
            return Optional.empty();
        }
        String computerName = dtoComputer.getName();
        return null;
    }

    private ComputerMapper() {
    }
}
