package com.excilys.mapper;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import com.excilys.model.Company;
import com.excilys.model.CompanyDto;


/**
 * Classe permettant le mapping entre Company et CompanyDTO.
 *
 * @author jguyot2
 *
 */
public final class CompanyMapper {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyMapper.class);


    /**
     * Conversion CompanyDTO > Company.
     *
     * @param companyDTO le companyDTO à convertir
     *
     * @return un optional contenant la valeur associée au DTO si la valeur du DTO est valide, un optional
     *         vide sinon
     */
    public static Optional<Company> companyDTOToCompany(@Nullable final CompanyDto companyDTO) {
        if (companyDTO == null) {
            LOG.debug("companyDTO > Company : param nul");
            return Optional.empty();
        }
        String name = companyDTO.getName();
        Long id = companyDTO.getId();
        return Optional.of(new Company(name, id));
    }

    /**
     * Conversion Company > CompanyDTO.
     *
     * @param company
     *
     * @return un optional contenant une instance de CompanyDTO correspondant au paramètre, ou un optional
     *         vide si le paramètre est nul ou la valeur invalide
     */
    public static Optional<CompanyDto> companyToDTO(@Nullable final Company company) {
        if (company == null) {
            return Optional.empty();
        }
        Long id = company.getId();
        String name = company.getName();
        return Optional.of(new CompanyDto(name, id));
    }

    private CompanyMapper() {
    }
}
