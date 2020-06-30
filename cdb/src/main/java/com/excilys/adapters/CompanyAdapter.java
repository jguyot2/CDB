package com.excilys.adapters;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.excilys.mapper.CompanyMapper;
import com.excilys.model.Company;
import com.excilys.model.CompanyDTO;
import com.excilys.model.Page;
import com.excilys.service.CompanyService;
import com.excilys.service.SearchValidator;

@Service
public final class CompanyAdapter implements SearchValidator<CompanyDTO> {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyAdapter.class);

    @Autowired
    private CompanyService companyValidator;

    /**
     * Récupération de la liste des valeurs.
     *
     * @return La liste des ordinateurs de la base sous forme de DTO
     */
    @Override
    public List<CompanyDTO> fetchList() {
        LOG.info("DTOCompany : fetchlist");
        List<Company> companyList = this.companyValidator.fetchList();
        return companyList.stream()
                .map(c -> CompanyMapper.companyToDTO(c).orElseThrow(IllegalArgumentException::new))
                .filter(dto -> dto != null).collect(Collectors.toList());
    }

    @Override
    public List<CompanyDTO> fetchList(@NonNull final Page page) {
        List<Company> companyList = this.companyValidator.fetchList(page);
        return companyList.stream().map(c -> CompanyMapper.companyToDTO(c).orElse(null))
                .filter(dto -> dto != null).collect(Collectors.toList());
    }

    @Override
    public Optional<CompanyDTO> findById(final long id) {
        LOG.info("Recherche de l'id " + id);
        Optional<Company> foundCompanyOpt = this.companyValidator.findById(id);
        if (foundCompanyOpt.isPresent()) {
            return CompanyMapper.companyToDTO(foundCompanyOpt.get());
        } else {
            LOG.debug("id non trouvé");
            return Optional.empty();
        }
    }

    @Override
    public int getNumberOfElements() {
        return this.companyValidator.getNumberOfElements();
    }

    public void setCompanyValidator(final CompanyService newCompanyValidator) {
        this.companyValidator = newCompanyValidator;
    }
}
