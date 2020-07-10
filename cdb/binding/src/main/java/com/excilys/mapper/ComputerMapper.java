package com.excilys.mapper;

import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.excilys.model.Company;
import com.excilys.model.CompanyDto;
import com.excilys.model.Computer;
import com.excilys.model.ComputerDto;

/**
 *
 * @author jguyot2
 */
public final class ComputerMapper {
    private static final Logger LOG = LoggerFactory.getLogger(ComputerMapper.class);

    /**
     * Conversion ComputerDTO > Computer.
     *
     * @param dtoComputer
     * @return un Optional contenant la valeur de Computer associée au DTO si ce dernier est
     *         cohérent (=les identifiants sont bien des nombres, et le nom n'est pas vide).
     */
    public static Optional<Computer> computerDTOToComputer(@NonNull final ComputerDto dtoComputer) {
        Company company = CompanyMapper.companyDTOToCompany(dtoComputer.getCompany()).orElse(null);
        return computerDTOToComputer(dtoComputer, company);
    }

    /**
     * Conversion ComputerDTO > Computer avec le champ "manufacturer" donné.
     *
     * @param dtoComputer  un DTO représentant un ordinateur.
     * @param manufacturer Le fabricant de l'ordinateur.
     * @return un optional contenant l'ordinateur associé au DTO, avec l'attribut "manufacturer"
     *         égal au second paramètre (pouvant être nul), ou un Optional vide si les valeurs du
     *         DTO sont incohérentes.
     */
    private static Optional<Computer> computerDTOToComputer(@Nullable final ComputerDto dtoComputer,
            @Nullable final Company manufacturer) {
        if (dtoComputer == null) {
            LOG.debug("DTO > computer : param nul");
            return Optional.empty();
        }
        Long id = dtoComputer.getId() == null ? 0 : dtoComputer.getId();
        String computerName = getNameFromComputerDTO(dtoComputer);
        Optional<LocalDate> intro = DateMapper.stringToLocalDate(dtoComputer.getIntroduced());
        Optional<LocalDate> discontinuation = DateMapper
                .stringToLocalDate(dtoComputer.getDiscontinued());

        return Optional.of(new Computer(computerName, manufacturer, intro.orElse(null),
                discontinuation.orElse(null), id));
    }

    /**
     * Conversion Computer > ComputerDTO.
     *
     * @param c un ordinateur à convertir
     * @return un optional contenant une instance de ComputerDTO correspondant à la
     *
     */
    public static Optional<ComputerDto> computerToDTO(@Nullable final Computer c) {
        if (c == null) {
            LOG.info("computer > DTO : param nul");
            return Optional.empty();
        }
        String name = c.getName();
        String id = c.getId() == 0 ? null : String.valueOf(c.getId());
        Optional<String> dateIntro = DateMapper.localDateToString(c.getIntroduction());
        Optional<String> dateDisco = DateMapper.localDateToString(c.getDiscontinuation());
        Optional<CompanyDto> company = CompanyMapper.companyToDTO(c.getManufacturer());
        return Optional.of(new ComputerDto(name, id, company.orElse(null), dateIntro.orElse(null),
                dateDisco.orElse(null)));
    }

    private static String getNameFromComputerDTO(@Nullable final ComputerDto dtoComputer) {
        String computerName = null;
        if (dtoComputer.getName() != null && !dtoComputer.getName().trim().isEmpty()) {
            computerName = dtoComputer.getName();
            LOG.trace("Nom du PC: " + computerName);
        }
        return computerName;
    }

    private ComputerMapper() {
    }
}
