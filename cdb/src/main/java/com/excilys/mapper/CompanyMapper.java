package com.excilys.mapper;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.model.Company;
import com.excilys.model.CompanyDTO;

/**
 * Classe permettant le mapping entre Company et CompanyDTO.
 * 
 * @author jguyot2
 *
 */
public final class CompanyMapper {
    /**
     */
    private static final Logger LOG = LoggerFactory.getLogger(CompanyMapper.class);

    /**
     * Conversion CompanyDTO > Company.
     * 
     * @param companyDTO le companyDTO à transmettre
     * @return un optional contenant la valeur associée au DTO.
     */
    public static Optional<Company> companyDTOToCompany(final CompanyDTO companyDTO) {
        if (companyDTO == null) {
            LOG.info("companyDTO > Company : param nul");
            return Optional.empty();
        }
        String name = null;
        if (companyDTO.getName() != null && !"".equals(companyDTO.getName().trim())) {
            name = companyDTO.getName();
        }
        long id = 0;
        if (companyDTO.getId() != null && !"".equals(companyDTO.getId())) {
            try {
                id = Long.parseLong(companyDTO.getId());
            } catch (NumberFormatException e) {
                LOG.error("Identifiant du CompanyDTO invalide");
                return Optional.empty();
            }
        }
        return Optional.of(new Company(name, id));
    }

    /**
     * @param company
     * @return un optional contenant une instance de CompanyDTO correspondant au
     *         paramètre, ou un optional vide sinon
     */
    public static Optional<CompanyDTO> companyToDTO(final Company company) {
        if (company == null) {
            return Optional.empty();
        }
        String id = company.getId() == 0 ? "" : String.valueOf(company.getId());
        String name = company.getName();
        return Optional.of(new CompanyDTO(name, id));
    }

    private CompanyMapper() {
    }
}
