package com.excilys.mapper;

import java.util.Optional;

import com.excilys.model.Company;
import com.excilys.model.CompanyDTO;

/**
 * Classe permettant le mapping entre Company et CompanyDTO.
 * @author jguyot2
 *
 */
public final class CompanyMapper {

   /**
    * @param companyDTO le companyDTO à transmettre
    * @return un optional contenant la valeur associée au DTO.
    */
    public static Optional<Company> companyDTOToCompany(final CompanyDTO companyDTO) {
        if (companyDTO == null) {
            return Optional.empty();
        }
        String name = null;
        if (companyDTO.getName() != null && !"".equals(companyDTO.getName().trim())) {
            name = companyDTO.getName().trim();
        }
        long id = 0;
        if (companyDTO.getId() != null) {
            try {
                id = Long.parseLong(companyDTO.getId());
            } catch (NumberFormatException e) {
            }
        }
        return Optional.of(new Company(name, id));
    }

    private CompanyMapper() {
    }
}
