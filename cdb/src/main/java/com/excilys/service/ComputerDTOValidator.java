package com.excilys.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.excilys.mapper.ComputerMapper;
import com.excilys.mapper.DateMapper;
import com.excilys.model.Company;
import com.excilys.model.CompanyDTO;
import com.excilys.model.Computer;
import com.excilys.model.ComputerDTO;
import com.excilys.model.Page;

public class ComputerDTOValidator implements SearchValidator<ComputerDTO> {
    private final CompanyDTOValidator companyDTOValidator = new CompanyDTOValidator();
    private final ComputerValidator computerValidator = new ComputerValidator();

    /**
     * Ajout d'un ordinateur dans la base à partir d'un DTO.
     *
     * @param computerDTO
     *
     * @return
     *
     * @throws InvalidComputerDTOException
     *                                              Si les
     *                                              champs du
     *                                              DTO ne sont
     *                                              pas des
     *                                              valeurs
     *                                              valides.
     *                                              Contient des
     *                                              valeurs de
     *                                              ComputerDTOProblems
     *                                              décrivant
     *                                              les valeurs
     *                                              posant
     *                                              problème
     * @throws InvalidComputerInstanceException
     *                                              si les
     *                                              champs sont
     *                                              valides,
     *                                              mais que
     *                                              l'instance
     *                                              de
     *                                              computer
     *                                              représentée.
     */
    public long addComputerDTO(final ComputerDTO computerDTO)
        throws InvalidComputerDTOException, InvalidComputerInstanceException {

        List<ComputerDTOProblems> dtoInstanceProblems = new ArrayList<>();
        Computer computer = getComputerFromDTOWithoutCompany(computerDTO, dtoInstanceProblems);
        Company company = getCompanyFromDTOById(computerDTO, dtoInstanceProblems).orElse(null);
        computer.setManufacturer(company);
        if (dtoInstanceProblems.size() > 0) {
            throw new InvalidComputerDTOException(dtoInstanceProblems);
        }
        return computerValidator.createComputer(computer);
    }

    @Override
    public List<ComputerDTO> fetchList() {
        return computerValidator.fetchList().stream()
            .map(c -> ComputerMapper.computerToDTO(c)
                .orElseThrow(() -> new IllegalArgumentException())) // TODO
                                                                    // mettre
                                                                    // une
                                                                    // exception
                                                                    // appropriée
            .collect(Collectors.toList());
    }

    @Override
    public List<ComputerDTO> fetchWithOffset(final Page page) {
        return computerValidator.fetchWithOffset(page).stream()
            .map(c -> ComputerMapper.computerToDTO(c)
                .orElseThrow(() -> new IllegalArgumentException())) // TODO
                                                                    // mettre
                                                                    // une
                                                                    // exception
                                                                    // appropréie
            .collect(Collectors.toList());
    }

    @Override
    public Optional<ComputerDTO> findById(final long id) {
        Optional<Computer> cv = computerValidator.findById(id);
        if (cv.isPresent())
            return ComputerMapper.computerToDTO(cv.get());
        else
            return Optional.empty();
    }

    @Override
    public int getNumberOfElements() {
        // TODO Auto-generated method stub
        return 0;
    }

    private Optional<Company> getCompanyFromDTOById(final ComputerDTO computerDTO,
        final List<ComputerDTOProblems> problems) {
        CompanyDTO companyDTO = computerDTO.getStrEntrepriseId();
        if (companyDTO == null || companyDTO.getId() == null || "0".equals(companyDTO.getId())
            || companyDTO.getId().isEmpty()) {
            return Optional.empty();
        }
        return companyDTOValidator.getCompanyFromCompanyDTOById(companyDTO, problems);
    }

    // TODO : javadoc
    private Computer getComputerFromDTOWithoutCompany(final ComputerDTO computerDTO,
        final List<ComputerDTOProblems> problems) {

        String name = getNameFromDTO(computerDTO, problems);
        LocalDate intro = getIntroductionDateFromDTO(computerDTO, problems).orElse(null);
        LocalDate disco = getDiscontinuationDateFromDTO(computerDTO, problems).orElse(null);
        long id = getIdFromDTO(computerDTO, problems);
        return new Computer(name, null, intro, disco, id);
    }

    private Optional<LocalDate> getDiscontinuationDateFromDTO(final ComputerDTO computerDTO,
        final List<ComputerDTOProblems> problems) {

        if (computerDTO.getDiscontinuationDate() == null
            || computerDTO.getDiscontinuationDate().isEmpty()) {
            return Optional.empty();
        }
        Optional<LocalDate> discoOpt = DateMapper
            .stringToLocalDate(computerDTO.getDiscontinuationDate());
        if (!discoOpt.isPresent()) {
            problems.add(ComputerDTOProblems.INVALID_DATE_DISCO);
        }
        return discoOpt;
    }

    private long getIdFromDTO(final ComputerDTO computerDTO,
        final List<ComputerDTOProblems> problems) {
        String idRepr = computerDTO.getStrId();
        if (idRepr == null || idRepr.isEmpty() || "0".equals(idRepr)) {
            return 0;
        }
        try {
            return Long.parseLong(idRepr);
        } catch (NumberFormatException e) {
            problems.add(ComputerDTOProblems.INVALID_ID);
            return 0;
        }
    }

    private Optional<LocalDate> getIntroductionDateFromDTO(final ComputerDTO computerDTO,
        final List<ComputerDTOProblems> problems) {

        if (computerDTO.getIntroductionDate() == null
            || computerDTO.getIntroductionDate().isEmpty()) {
            return Optional.empty();
        }
        Optional<LocalDate> introOpt =
            DateMapper.stringToLocalDate(computerDTO.getIntroductionDate());
        if (!introOpt.isPresent()) {
            problems.add(ComputerDTOProblems.INVALID_DATE_INTRO);
        }
        return introOpt;
    }

    private String getNameFromDTO(final ComputerDTO computerDTO,
        final List<ComputerDTOProblems> problems) {

        if (computerDTO.getName() == null || computerDTO.getName().isEmpty()) {
            problems.add(ComputerDTOProblems.INVALID_NAME);
            return "";
        }
        return computerDTO.getName();
    }
}
