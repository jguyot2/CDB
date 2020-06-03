package com.excilys.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.excilys.mapper.CompanyMapper;
import com.excilys.model.Company;
import com.excilys.model.CompanyDTO;
import com.excilys.model.Page;

public class CompanyDTOValidator implements SearchValidator<CompanyDTO> {
    private CompanyValidator companyValidator = new CompanyValidator();

    /**
     * Récupération de la liste des valeurs
     * @return La liste des ordinateurs de la base sous forme de DTO
     */
    @Override
    public List<CompanyDTO> fetchList() {
        List<Company> companyList = companyValidator.fetchList();
        return companyList.stream()
            .map(c -> CompanyMapper.companyToDTO(c)
                .orElseThrow(() -> new IllegalArgumentException()))
            .filter(dto -> dto != null)
            .collect(Collectors.toList());
    }

    @Override
    public List<CompanyDTO> fetchWithOffset(final Page page) {
        List<Company> companyList = companyValidator.fetchWithOffset(page);
        return companyList.stream()
            .map(c -> CompanyMapper.companyToDTO(c).orElse(null))
            .filter(dto -> dto != null)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<CompanyDTO> findById(final long id) {
        Optional<Company> foundCompanyOpt = companyValidator.findById(id);
        if (foundCompanyOpt.isPresent()) {
            return CompanyMapper.companyToDTO(foundCompanyOpt.get());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Recherche d'une entreprise dans la BD à partir d'une instance de DTO contenant un
     * champ identifiant rempli.
     *
     * Si l'instance est invalide (le champ n'est pas un chiffre ou l'id n'existe pas),
     * ajoute une valeur décrivant le problème dans la liste en paramètre.
     * @param companyDTO instance non-nulle décrivant une entreprise, qui contient un champ
     * id rempli.
     * @param problems Liste non nulleoù ajouter les valeurs décrivant les
     * problèmes s'il y en a
     * @return un Optional vide si la valeur de l'identifiant est invalide ou nulle,
     * Un optional contenant la valeur de l'entreprise dans la base sinon.
     */
    public Optional<Company> getCompanyFromCompanyDTOById(final CompanyDTO companyDTO,
        final List<ComputerDTOProblems> problems) {
        if (companyDTO == null) {
            return Optional.empty();
        }
        String idRepr = companyDTO.getId();
        try {
            long researchedId = Long.parseLong(idRepr, 10);
            Optional<Company> foundCompanyOpt = companyValidator.findById(researchedId);
            if (!foundCompanyOpt.isPresent()) {
                problems.add(ComputerDTOProblems.INEXISTANT_COMPANY_ID);
            }
            return foundCompanyOpt;
        } catch (NumberFormatException e) {
            problems.add(ComputerDTOProblems.INVALID_MANUFACURER_ENTRY);
            return Optional.empty();
        }
    }

    @Override
    public int getNumberOfElements() {
        return companyValidator.getNumberOfElements();
    }

    public void setCompanyValidator(final CompanyValidator newCompanyValidator) {
        companyValidator = newCompanyValidator;
    }
}
