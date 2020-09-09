package com.excilys.adapters;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import com.excilys.mapper.ComputerMapper;
import com.excilys.model.Computer;
import com.excilys.model.ComputerDto;
import com.excilys.model.Page;
import com.excilys.model.sort.DuplicatedSortEntriesException;
import com.excilys.model.sort.SortEntry;
import com.excilys.service.ComputerService;
import com.excilys.service.InvalidComputerException;
import com.excilys.service.SearchValidator;


/**
 * Classe s'assurant que les requêtes/mises à jour liées à des instances de ComputerDTO sont bien formées (i.e
 * les instances passées en paramètre sont valides) avant de réaliser ces mises à jour
 *
 * @author jguyot2
 *
 */
@Service
public class ComputerAdapter implements SearchValidator<ComputerDto> {

    @Autowired
    private ComputerDtoValidator dtoInstanceValidator;

    @Autowired
    private ComputerService computerService;


    private static List<ComputerDto> convertList(@NonNull final List<Computer> l) {
        return l.stream().map(c -> ComputerMapper.computerToDTO(c).get()).collect(Collectors.toList());
    }

    /**
     * Ajout d'un ordinateur dans la base à partir d'un DTO.
     *
     * @param computerDTO
     *
     *
     * @throws InvalidComputerDtoException Si les champs du DTO ne sont pas des valeurs valides. Contient des
     *                                     valeurs de ComputerDTOProblems décrivant les valeurs posant
     *                                     problème
     * @throws InvalidComputerException    si les champs sont valides, mais que l'instance de computer
     *                                     représentée ne l'est pas.
     *
     * @return l'identifiant de l'ordinateur ajouté si la mise à jour a réussi, 0 sinon.
     */
    public long addComputerDTO(@Nullable final ComputerDto computerDTO)
            throws InvalidComputerDtoException, InvalidComputerException {
        Computer computer = validateToComputer(computerDTO);
        return this.computerService.addComputer(computer);
    }

    /**
     * Suppression d'un ordinateur de la base, dont l'id est en paramètre
     *
     * @param id
     *
     * @return 1 si le pc a été supprimé, 0 si pas de pc avec cet id -1 s'il y a eu un problème dans la base
     */
    public int delete(final Long... identifiers) {
        return this.computerService.delete(identifiers);
    }

    @Override
    public List<ComputerDto> fetchList() {
        return convertList(this.computerService.fetchList());
    }

    @Override
    public List<ComputerDto> fetchList(@Nullable final Page page) {
        return convertList(this.computerService.fetchList(page));
    }

    public List<ComputerDto> fetchList(@Nullable final Page p, @Nullable final List<SortEntry> sortEntries)
            throws DuplicatedSortEntriesException {
        return convertList(this.computerService.fetchList(p, sortEntries));
    }

    List<ComputerDto> fetchList(final List<SortEntry> sortEntries) throws DuplicatedSortEntriesException {
        return convertList(this.computerService.fetchList(sortEntries));
    }

    public List<ComputerDto> fetchList(@NonNull final Page p, @NonNull final String searchedName) {
        return convertList(this.computerService.fetchList(p, searchedName));
    }

    public List<ComputerDto> fetchList(@NonNull final Page p, @NonNull final String search,
            @NonNull final List<SortEntry> sortEntries) throws DuplicatedSortEntriesException {

        return convertList(this.computerService.fetchList(p, search, sortEntries));
    }

    public List<ComputerDto> fetchList(@NonNull final String search) {
        return convertList(this.computerService.fetchList(search));

    }

    @Override
    public Optional<ComputerDto> findById(final long id) {
        return this.computerService.findById(id)
                .map((final Computer cpt) -> ComputerMapper.computerToDTO(cpt).get());
    }

    @Override
    public int getNumberOfElements() {
        return this.computerService.getNumberOfElements();
    }

    /**
     * Recherche du nombre d'élements de la base correspondant à la recherche en param
     *
     * @param search
     *
     * @return
     */
    public int getNumberOfFoundElements(@Nullable final String search) {
        return getNumberOfElements();

    }

    public int updateComputer(@Nullable final ComputerDto computerValue)
            throws InvalidComputerDtoException, InvalidComputerException {

        Computer computer = validateToComputer(computerValue);
        return this.computerService.update(computer);
    }

    void setService(final ComputerService newService) {
        this.computerService = newService;
    }

    private Computer validateToComputer(final ComputerDto dtoInstance) throws InvalidComputerDtoException {
        this.dtoInstanceValidator.validate(dtoInstance);
        return ComputerMapper.computerDTOToComputer(dtoInstance).get();
    }
}
