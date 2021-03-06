package com.excilys.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import com.excilys.model.Computer;
import com.excilys.model.Page;
import com.excilys.model.sort.DuplicatedSortEntriesException;
import com.excilys.model.sort.SortEntry;
import com.excilys.persistence.ComputerSearcher;
import com.excilys.persistence.ComputerUpdater;
import com.excilys.persistence.DaoException;

/**
 * Classe validant les requêtes/mises à jour avant de les envoyer au paquet
 * persistance.
 *
 * @author jguyot2
 */
@Service
public class ComputerService implements SearchValidator<Computer> {

    private static final Logger LOG = LoggerFactory.getLogger(ComputerService.class);

    @Autowired
    private ComputerValidator validator;

    @Autowired
    private ComputerSearcher computerSearcher;

    @Autowired
    private ComputerUpdater computerUpdater;

    void setValidator(final ComputerValidator v) {
        this.validator = v;
    }

    /**
     * Ajout d'un ordinateur dans la base.
     *
     * @param createdComputer l'ordinateur ajouté.
     *
     * @return 0 si la création n'a pas pu se faire dans la BD, ou le nouvel
     *         identifiant qui vient d'être créé
     *
     * @throws InvalidComputerException si l'instance en paramètre
     */
    public long addComputer(@Nullable final Computer createdComputer) throws InvalidComputerException {
        this.validator.validate(createdComputer);
        try {
            return this.computerUpdater.createComputer(createdComputer);
        } catch (DaoException e) {
            LOG.error("createComputer :" + e.getMessage(), e);
            return 0;
        }
    }

    public int delete(final Long... identifiers) {
        try {
            int result = 0;
            for (long id : identifiers) {
                result += this.computerUpdater.deleteById(identifiers);
            }
            return result;
        } catch (DaoException e) {
            LOG.error("deleteComputer :" + e.getMessage(), e);
            return -1;
        }
    }

    /**
     * Recherche de la liste de tous les ordinateurs présents dans la base de
     * données.
     *
     * @return La liste des ordinateurs présents dans la base de données, ou une
     *         liste vide en cas d'erreur.
     */
    @Override
    public List<Computer> fetchList() {
        try {
            return this.computerSearcher.fetchList();
        } catch (DaoException e) {
            LOG.error("fetchlist: " + e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * @param page la page à afficher
     *
     * @return la liste des ordinateurs présents sur la page.
     */
    @Override
    public List<Computer> fetchList(@NonNull final Page page) {
        try {
            return this.computerSearcher.fetchList(page);
        } catch (DaoException e) {
            LOG.error("fetchListWithOffset: " + e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public List<Computer> fetchList(@NonNull final Page p, @NonNull final List<SortEntry> sortEntries)
            throws DuplicatedSortEntriesException {
        try {
            if (areSortEntriesValid(sortEntries)) {
                return this.computerSearcher.fetchList(p, sortEntries);
            } else {
                throw new DuplicatedSortEntriesException();
            }
        } catch (DaoException e) {
            LOG.error("Erreur lors du fetchWithOrder", e);
            return new ArrayList<>();
        }
    }

    public List<Computer> fetchList(@NonNull final Page p, @NonNull final String search) {
        try {
            return this.computerSearcher.fetchList(p, search);
        } catch (DaoException e) {
            LOG.error("", e);
            return Arrays.asList();
        }
    }

    private boolean areSortEntriesValid(final List<SortEntry> sortEntries) {
        for (int i = 0; i < sortEntries.size(); ++i) {
            for (int j = 0; j < sortEntries.size(); ++j) {
                if (i != j && sortEntries.get(i).getCriteria() == sortEntries.get(j).getCriteria()) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Computer> fetchList(@NonNull final Page p, @NonNull final String search,
            @NonNull final List<SortEntry> sortEntries) throws DuplicatedSortEntriesException {
        try {
            if (areSortEntriesValid(sortEntries)) {
                return this.computerSearcher.fetchList(p, search, sortEntries);
            } else {
                throw new DuplicatedSortEntriesException();
            }
        } catch (DaoException e) {
            LOG.error("", e);
            return new ArrayList<>();
        }
    }

    public List<Computer> fetchList(@NonNull final String search) {
        try {
            return this.computerSearcher.searchByName(search);
        } catch (DaoException e) {
            LOG.error("", e);
            return new ArrayList<>();
        }
    }

    /**
     * Recherche d'une instance de Computer dans la base à partir de son
     * identifiant.
     *
     * @param id l'identifiant du computer recherché dans la base de donné
     *
     * @return Optional.empty() si la recherche a échoué, ou un Optional contenant
     *         la valeur de l'identifiant recherché sinon
     */
    @Override
    public Optional<Computer> findById(final long id) {
        try {
            return this.computerSearcher.fetchById(id);
        } catch (DaoException e) {
            LOG.error("", e);
            return Optional.empty();
        }

    }

    /**
     * @return le nombre d'ordinateurs dans la base
     */
    @Override
    public int getNumberOfElements() {
        try {
            return this.computerSearcher.getNumberOfElements();
        } catch (DaoException e) {
            LOG.error("", e);
            return -1;
        }
    }

    public int getNumberOfFoundElements(@NonNull final String search) {
        try {
            return this.computerSearcher.getNumberOfFoundElements(search);
        } catch (DaoException e) {
            LOG.error("", e);
            return -1;
        }
    }

    /**
     * Changement des attributs associés à la persistance, uniquement utilisé pour
     * des tests avec des classes mock.
     *
     * @param newComputerSearcher
     * @param newComputerUpdater
     */
    public void setComputerSearcher(final ComputerSearcher newComputerSearcher,
            final ComputerUpdater newComputerUpdater) {
        this.computerSearcher = newComputerSearcher;
        this.computerUpdater = newComputerUpdater;
    }

    /**
     * Mise à jour de l'instance de Computer passée en paramètre.
     *
     * @param newComputervalue la nouvelle valeur de l'instance, avec un identifiant
     *                         défini.
     *
     * @return -1 si exception lors de la requête, 0 si pas de mise à jour, ou 1 si
     *         la mise à jour a eu lieu
     *
     * @throws InvalidComputerException Si l'instance en paramètre n'est pas valide
     */
    public int update(@NonNull final Computer newComputervalue) throws InvalidComputerException {
        LOG.trace("Mise à jour d'un ordinateur dans la base. Test de validité");
        this.validator.validate(newComputervalue);
        LOG.trace("Instance valide : Mise à jour de la base.");
        try {
            return this.computerUpdater.updateComputer(newComputervalue);
        } catch (DaoException e) {
            LOG.error("updateComputer :" + e.getMessage(), e);
            return -1;
        }
    }

    public List<Computer> fetchList(@Nullable final List<SortEntry> sortEntries)
            throws DuplicatedSortEntriesException {
        if (sortEntries == null || sortEntries.isEmpty()) {
            return this.fetchList();
        }
        try {
            if (areSortEntriesValid(sortEntries)) {
                return this.computerSearcher.fetchList(sortEntries);
            } else {
                throw new DuplicatedSortEntriesException();
            }
        } catch (DaoException e) {
            LOG.error("Database error", e);
            return new ArrayList<>();
        }
    }
}
