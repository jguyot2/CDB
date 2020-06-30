package com.excilys.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.mapper.ComputerMapper;
import com.excilys.mapper.DateMapper;
import com.excilys.model.Company;
import com.excilys.model.CompanyDTO;
import com.excilys.model.Computer;
import com.excilys.model.ComputerDTO;
import com.excilys.model.Page;
import com.excilys.model.SortEntry;

/**
 * Classe s'assurant que les requêtes/mises à jour liées à des instances de
 * ComputerDTO sont bien formées (i.e les instances passées en paramètre sont
 * valides) avant de réaliser ces mises à jour
 *
 * @author jguyot2
 *
 */
@Service
public class ComputerDTOValidator implements SearchValidator<ComputerDTO> {
    private static List<ComputerDTO> convertList(final List<Computer> l) {
        return l.stream().map(c -> ComputerMapper.computerToDTO(c).get())
                .collect(Collectors.toList());
    }

    /**
     * Récupération d'un ordi sans l'attribut "company" à partir d'un DTO.
     *
     * @param computerDTO le DTO à "convertir"
     * @param problems    L'éventuelle liste des problèmes associés à la DTO en
     *                    paramètre, à laquelle on ajoute des éléments s'il y a des
     *                    problèmes.
     * @return Une instance de Computer associée à computerDTO
     */
    private static Computer getComputerFromDTOWithoutCompany(final ComputerDTO computerDTO,
            final List<ComputerDTOProblems> problems) {

        String name = getNameFromDTO(computerDTO, problems);
        LocalDate intro = getIntroductionDateFromDTO(computerDTO, problems).orElse(null);
        LocalDate disco = getDiscontinuationDateFromDTO(computerDTO, problems).orElse(null);
        long id = getIdFromDTO(computerDTO, problems);
        return new Computer(name, null, intro, disco, id);
    }

    /**
     *
     * @param computerDTO
     * @param problems
     * @return
     */
    private static Optional<LocalDate> getDiscontinuationDateFromDTO(final ComputerDTO computerDTO,
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

    /**
     * Récupération de l'id du dto, ajout d'un élément dans la liste problems s'il y
     * a une anomalie
     *
     * @param computerDTO
     * @param problems
     * @return
     */
    private static long getIdFromDTO(final ComputerDTO computerDTO,
            final List<ComputerDTOProblems> problems) {
        String idRepr = computerDTO.getId();
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

    /**
     *
     * @param computerDTO
     * @param problems
     * @return
     */
    private static Optional<LocalDate> getIntroductionDateFromDTO(final ComputerDTO computerDTO,
            final List<ComputerDTOProblems> problems) {

        if (computerDTO.getIntroductionDate() == null
                || computerDTO.getIntroductionDate().isEmpty()) {
            return Optional.empty();
        }
        Optional<LocalDate> introOpt = DateMapper
                .stringToLocalDate(computerDTO.getIntroductionDate());
        if (!introOpt.isPresent()) {
            problems.add(ComputerDTOProblems.INVALID_DATE_INTRO);
        }
        return introOpt;
    }

    /**
     * @param computerDTO
     * @param problems
     * @return le nom du DTO
     */
    private static String getNameFromDTO(final ComputerDTO computerDTO,
            final List<ComputerDTOProblems> problems) {

        if (computerDTO.getName() == null || computerDTO.getName().isEmpty()) {
            problems.add(ComputerDTOProblems.INVALID_NAME);
            return "";
        }
        return computerDTO.getName();
    }

    @Autowired
    private CompanyDTOValidator companyDTOValidator;
    @Autowired
    private ComputerValidator computerValidator;

    private ComputerDTOValidator() {
    }

    /**
     * Ajout d'un ordinateur dans la base à partir d'un DTO.
     *
     * @param computerDTO
     *
     *
     * @throws InvalidComputerDTOException      Si les champs du DTO ne sont pas des
     *                                          valeurs valides. Contient des
     *                                          valeurs de ComputerDTOProblems
     *                                          décrivant les valeurs posant
     *                                          problème
     * @throws InvalidComputerInstanceException si les champs sont valides, mais que
     *                                          l'instance de computer représentée
     *                                          ne l'est pas.
     * @return l'identifiant de l'ordinateur ajouté si la mise à jour a réussi, 0
     *         sinon.
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
        return this.computerValidator.addComputer(computer);
    }

    /**
     * Suppression d'un ordinateur de la base, dont l'id est en paramètre
     *
     * @param id
     * @return 1 si le pc a été supprimé, 0 si pas de pc avec cet id -1 s'il y a eu
     *         un problème dans la base
     */
    public int delete(final long id) {
        return this.computerValidator.delete(id);
    }

    @Override
    public List<ComputerDTO> fetchList() {
        return convertList(this.computerValidator.fetchList());
    }

    @Override
    public List<ComputerDTO> fetchList(final Page page) {
        return convertList(this.computerValidator.fetchList(page));
    }

    public List<ComputerDTO> fetchList(final Page p, final List<SortEntry> sortEntries)
            throws DuplicatedSortEntries {
        return convertList(this.computerValidator.fetchList(p, sortEntries));
    }

    public List<ComputerDTO> fetchList(final Page p, final String searchedName) {
        return convertList(this.computerValidator.fetchList(p, searchedName));

    }

    public List<ComputerDTO> fetchList(final Page p, final String search,
            final List<SortEntry> sortEntries) throws DuplicatedSortEntries {
        return convertList(this.computerValidator.fetchList(p, search, sortEntries));
    }

    public List<ComputerDTO> fetchList(final String search) {
        return convertList(this.computerValidator.fetchList(search));
    }

    @Override
    public Optional<ComputerDTO> findById(final long id) {
        Optional<Computer> cv = this.computerValidator.findById(id);
        if (cv.isPresent()) {
            return ComputerMapper.computerToDTO(cv.get());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Récupération d'une instance de Company à partir de l'identifiant compris dans
     * le DTO en paramètre.
     *
     * @param computerDTO le dto de l'ordinateur
     * @param problems    liste de problèmes, mise à jour à la rencontre d'une
     *                    erreur
     * @return Un optional contenant une Company si l'id correspond à une
     *         entreprise, ou vide s'il n'y a aucune entreprise associée
     */
    private Optional<Company> getCompanyFromDTOById(final ComputerDTO computerDTO,
            final List<ComputerDTOProblems> problems) {
        CompanyDTO companyDTO = computerDTO.getCompany();
        if (companyDTO == null || companyDTO.getId() == null || "0".equals(companyDTO.getId())
                || companyDTO.getId().isEmpty()) {
            return Optional.empty();
        }
        return this.companyDTOValidator.getCompanyFromCompanyDTOById(companyDTO, problems);
    }

    @Override
    public int getNumberOfElements() {
        return this.computerValidator.getNumberOfElements();
    }

    /**
     * Recherche du nombre d'élements de la base correspondant à la recherche en
     * param
     *
     * @param search
     * @return
     */
    public int getNumberOfFoundElements(final String search) {
        return this.computerValidator.getNumberOfFoundElements(search);
    }

    public int updateComputer(final ComputerDTO computerValue)
            throws InvalidComputerDTOException, InvalidComputerInstanceException {
        List<ComputerDTOProblems> problems = new ArrayList<>();
        Computer computer = getComputerFromDTOWithoutCompany(computerValue, problems);
        Company company = getCompanyFromDTOById(computerValue, problems).orElse(null);
        computer.setManufacturer(company);
        if (problems.size() > 0) {
            throw new InvalidComputerDTOException(problems);
        }
        return this.computerValidator.update(computer);
    }
}
