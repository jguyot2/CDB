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
import com.excilys.model.CompanyDto;
import com.excilys.model.Page;
import com.excilys.service.CompanyService;
import com.excilys.service.SearchValidator;


@Service
public final class CompanyAdapter implements SearchValidator<CompanyDto> {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyAdapter.class);

    @Autowired
    private CompanyService service;


    /**
     * Récupération de la liste des valeurs.
     *
     * @return La liste des ordinateurs de la base sous forme de DTO
     */
    @Override
    public List<CompanyDto> fetchList() {
        LOG.info("DTOCompany : fetchlist");
        List<Company> companyList = this.service.fetchList();
        return companyList.stream()
                .map(c -> CompanyMapper.companyToDTO(c).orElseThrow(IllegalArgumentException::new))
                .filter(dto -> dto != null).collect(Collectors.toList());
    }

    @Override
    public List<CompanyDto> fetchList(@NonNull final Page page) {
        List<Company> companyList = this.service.fetchList(page);
        return companyList.stream().map(c -> CompanyMapper.companyToDTO(c).orElse(null))
                .filter(dto -> dto != null).collect(Collectors.toList());
    }

    @Override
    public Optional<CompanyDto> findById(final long id) {
        LOG.info("Recherche de l'id " + id);
        Optional<Company> foundCompanyOpt = this.service.findById(id);
        if (foundCompanyOpt.isPresent()) {
            return CompanyMapper.companyToDTO(foundCompanyOpt.get());
        } else {
            LOG.debug("id non trouvé");
            return Optional.empty();
        }
    }

    @Override
    public int getNumberOfElements() {
        return this.service.getNumberOfElements();
    }

    public void setCompanyValidator(final CompanyService newCompanyValidator) {
        this.service = newCompanyValidator;
    }
}
