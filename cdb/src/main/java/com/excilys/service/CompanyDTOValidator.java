package com.excilys.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.mapper.CompanyMapper;
import com.excilys.model.Company;
import com.excilys.model.CompanyDTO;
import com.excilys.model.Page;

public final class CompanyDTOValidator implements SearchValidator<CompanyDTO> {
    /**
     */
    private static final Logger LOG = LoggerFactory.getLogger(CompanyDTOValidator.class);
    /**
     */
    private CompanyValidator companyValidator = new CompanyValidator();

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
                .map(c -> CompanyMapper.companyToDTO(c).orElseThrow(() -> new IllegalArgumentException()))
                .filter(dto -> dto != null).collect(Collectors.toList());
    }

    @Override
    public List<CompanyDTO> fetchWithOffset(final Page page) {
        List<Company> companyList = this.companyValidator.fetchWithOffset(page);
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

    /**
     * Recherche d'une entreprise dans la BD à partir d'une instance de DTO contenant
     * un champ identifiant rempli.
     *
     * Si l'instance est invalide (le champ n'est pas un chiffre ou l'id n'existe
     * pas), ajoute une valeur décrivant le problème dans la liste en paramètre.
     *
     * @param companyDTO instance non-nulle décrivant une entreprise, qui contient un
     *        champ id rempli.
     * @param problems Liste non nulleoù ajouter les valeurs décrivant les problèmes
     *        s'il y en a
     * @return un Optional vide si la valeur de l'identifiant est invalide ou nulle,
     *         Un optional contenant la valeur de l'entreprise dans la base sinon.
     */
    public Optional<Company> getCompanyFromCompanyDTOById(final CompanyDTO companyDTO,
            final List<ComputerDTOProblems> problems) {
        if (companyDTO == null) {
            LOG.info("GetCompanyFromCompanyDtoById : param nul");
            return Optional.empty();
        }
        String idRepr = companyDTO.getId();
        try {
            long researchedId = Long.parseLong(idRepr, 10);
            Optional<Company> foundCompanyOpt = this.companyValidator.findById(researchedId);
            if (!foundCompanyOpt.isPresent()) {
                LOG.debug("Entreprise non trouvée dans la base");
                problems.add(ComputerDTOProblems.INEXISTANT_COMPANY_ID);
            }
            return foundCompanyOpt;
        } catch (NumberFormatException e) {
            LOG.debug("Identifiant invalide");
            problems.add(ComputerDTOProblems.INVALID_MANUFACURER_ENTRY);
            return Optional.empty();
        }
    }

    @Override
    public int getNumberOfElements() {
        return this.companyValidator.getNumberOfElements();
    }

    /**
     * @param newCompanyValidator
     */
    public void setCompanyValidator(final CompanyValidator newCompanyValidator) {
        this.companyValidator = newCompanyValidator;
    }
}
