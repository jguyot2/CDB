package com.excilys.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.model.Computer;
import com.excilys.model.Page;
import com.excilys.persistence.ComputerSearcher;
import com.excilys.persistence.ComputerUpdater;

/**
 * Classe validant les requêtes/mises à jour avant de les envoyer au paquet
 * persistance.
 *
 * @author jguyot2
 */
public class ComputerValidator implements SearchValidator<Computer> {
    /** */
    private static final Logger LOG =
            LoggerFactory.getLogger(ComputerValidator.class);

    /** */
    private ComputerSearcher computerSearcher;
    /** */
    private ComputerUpdater computerUpdater;

    /** */
    public ComputerValidator() {
        this.computerSearcher = new ComputerSearcher();
        this.computerUpdater = new ComputerUpdater();
    }


    /**
     * Ajout d'un ordinateur dans la base.
     * @param createdComputer l'ordinateur ajouté.
     * @return 0 si la création n'a pas pu se faire dans la BD, ou le nouvel
     *         identifiant qui vient d'être créé
     * @throws InvalidComputerInstanceException si l'instance en paramètre
     */
    public long createComputer(final Computer createdComputer)
            throws InvalidComputerInstanceException {

        List<ComputerInstanceProblems> problems =
                getComputerInstanceProblems(createdComputer);

        if (problems.size() > 0) {
            LOG.debug("createComputer : instance invalide");
            throw new InvalidComputerInstanceException(problems);
        }

        try {
            return computerUpdater.createComputer(createdComputer);
        } catch (SQLException e) {
            LOG.error("createComputer :" + e.getMessage(), e);
            return 0;
        }
    }

    /**
     * Suppression d'un ordinateur de la base à partir de son identifiant.
     * @param id l'identifiant de l'ordinateur à supprimer.
     * @return 1 si l'ordi a été supprimé, 0 sinon.
     */
    public int deleteComputer(final long id) {
        try {
            return computerUpdater.deleteById(id);
        } catch (SQLException e) {
            LOG.error("deleteComputer :" + e.getMessage(), e);
            return -1; // TODO : gestion propre des erreurs
        }
    }

    /**
     * Recherche de la liste de tous les ordinateurs présents dans
     * la base de données.
     *
     * @return La liste des ordinateurs présents dans la base de données,
     * ou une liste vide en cas d'erreur.
     */
    public List<Computer> fetchList() {
        try {
            return computerSearcher.fetchList();
        } catch (SQLException e) {
            LOG.error("fetchlist: " + e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     *
     * @param page la page à afficher
     * @return la liste des ordinateurs présents sur la page.
     */
    public List<Computer> fetchWithOffset(final Page page) {
        try {
            return computerSearcher.fetchWithOffset(page);
        } catch (SQLException e) {
            LOG.error("fetchListWithOffset: " + e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Recherche d'une instance de Computer dans la base à partir de
     * son identifiant.
     *
     * @param id l'identifiant du computer recherché dans la base de donné
     * @return Optional.empty() si la recherche a échoué, ou un
     * Optional contenant
     * la valeur de l'identifiant recherché sinon
     */
    public Optional<Computer> findById(final long id) {
        try {
            return computerSearcher.fetchById(id);
        } catch (SQLException e) {
            LOG.error("findbyId(id = " + id + "): " + e.getMessage(), e);
            return Optional.empty();
        }
    }

    /**
     *
     * @param computer l'instance de Computer à tester.
     * @return Une liste contenant la liste des problèmes sur l'instance de Computer passée
     * en paramètre
     */
    private List<ComputerInstanceProblems> getComputerInstanceProblems(final Computer computer) {
        List<ComputerInstanceProblems> problems = new ArrayList<>();

        if (computer.getName() == null || computer.getName().trim().isEmpty()) {
            problems.add(ComputerInstanceProblems.INVALID_NAME);
        }
        if (computer.getIntroduction() != null) {
            if (computer.getDiscontinuation() != null
                    && computer.getIntroduction().compareTo(computer.getDiscontinuation()) > 0) {
                problems.add(ComputerInstanceProblems.INVALID_DISCONTINUATION_DATE);
            }
         }
        if (computer.getIntroduction() == null && computer.getDiscontinuation() != null) {
            problems.add(ComputerInstanceProblems.NULL_INTRO_WITH_NOT_NULL_DISCONTINUATION);
        }
        return problems;
    }

    /**
     * @return le nombre d'ordinateurs dans la base
     */
    public int getNumberOfElements() {
        try {
            return computerSearcher.getNumberOfElements();
        } catch (SQLException e) {
            LOG.error("getNumberOfElements : " + e.getMessage(), e);
            return -1;
        }
    }

    /**
     * Changement des attributs associés à la persistance, uniquement utilisé pour des tests
     * avec des classes mock.
     * @param newComputerSearcher
     * @param newComputerUpdater
     */
    public void setComputerSearcher(
            final ComputerSearcher newComputerSearcher,
            final ComputerUpdater newComputerUpdater) {
        this.computerSearcher = newComputerSearcher;
        this.computerUpdater = newComputerUpdater;
    }

    /**
     * Mise à jour de l'instance de Computer passée en paramètre.
     *
     * @param newComputervalue la nouvelle valeur de l'instance, avec un identifiant défini.
     * @return -1 si exception lors de la requête, 0 si pas de mise à jour, ou 1 si
     *         la mise à jour a eu lieu
     * @throws InvalidComputerInstanceException Si l'instance en paramètre n'est pas
     *                                          valide
     */
    public int updateComputer(final Computer newComputervalue)
            throws InvalidComputerInstanceException {

        List<ComputerInstanceProblems> problems = getComputerInstanceProblems(newComputervalue);

        if (problems.size() > 0) {
            LOG.debug("Mise à jour incorrecte du PC");
            throw new InvalidComputerInstanceException(problems);
        }

        try {
            return computerUpdater.updateComputer(newComputervalue);
        } catch (SQLException e) {
            LOG.error("updateComputer :" + e.getMessage(), e);
            //TODO gestion propre des erreurs ici
            return -1;
        }
    }
}
