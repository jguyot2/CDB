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
     * @param dtoComputer un ComputerDto valide (= nom non nul, chaînes bien formées).
     *
     * @return un Optional contenant la valeur de Computer associée au DTO
     */
    public static Optional<Computer> computerDTOToComputer(@Nullable final ComputerDto dtoComputer) {
        if (dtoComputer == null) {
            return Optional.empty();
        }
        Company company = CompanyMapper.companyDTOToCompany(dtoComputer.getCompany()).orElse(null);
        return computerDTOToComputer(dtoComputer, company);
    }

    private static Optional<Computer> computerDTOToComputer(@NonNull final ComputerDto dtoComputer,
            @Nullable final Company manufacturer) {
        Long id = dtoComputer.getId();
        String computerName = dtoComputer.getName();
        Optional<LocalDate> intro = DateMapper.stringToLocalDate(dtoComputer.getIntroduced());
        Optional<LocalDate> discontinuation = DateMapper
                .stringToLocalDate(dtoComputer.getDiscontinued());
        return Optional.of(new Computer(computerName, manufacturer, intro.orElse(null),
                discontinuation.orElse(null), id));
    }

    /**
     * Conversion Computer > ComputerDTO.
     *
     * @param c un ordinateur valide à convertir
     *
     * @return un optional contenant une instance de ComputerDTO correspondant à la
     *
     */
    public static Optional<ComputerDto> computerToDTO(@Nullable final Computer c) {
        if (c == null) {
            LOG.info("computer > DTO : param nul");
            return Optional.empty();
        }
        String name = c.getName();
        Optional<String> dateIntro = DateMapper.localDateToString(c.getIntroduction());
        Optional<String> dateDisco = DateMapper.localDateToString(c.getDiscontinuation());
        Optional<CompanyDto> company = CompanyMapper.companyToDTO(c.getManufacturer());
        return Optional.of(new ComputerDto(name, c.getId(), company.orElse(null), dateIntro.orElse(null),
                dateDisco.orElse(null)));
    }

    private ComputerMapper() {
    }
}
