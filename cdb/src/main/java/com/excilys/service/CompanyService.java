package com.excilys.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.excilys.model.Company;
import com.excilys.model.Page;
import com.excilys.persistence.CompanySearcher;
import com.excilys.persistence.CompanyUpdater;
import com.excilys.persistence.PersistanceException;

/**
 * Classe vérifiant que les requêtes sur les entreprises sont bien formée et
 * cohérentes.
 *
 * @author jguyot2
 *
 */
@Service
public class CompanyService implements SearchValidator<Company> {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyService.class);

    @Autowired
    private CompanySearcher companySearcher;

    @Autowired
    private CompanyUpdater companyUpdater;

    /**
     * Suppression d'une entreprise à partir de son identifiant
     *
     * @param companyId l'identifiant de l'entreprise à supprimer
     * @return 1 si l'entreprise a été supprimée, 0 si l'identifiant n'existe pas,
     *         -1 s'il y a eu une erreur dans la base
     */
    public int deleteCompanyById(final long companyId) {
        try {
            return this.companyUpdater.deleteCompany(companyId);
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
            return -1;
        }
    }

    /**
     * Fonction renvoyant la liste des entreprises présentes dans la BD.
     *
     * @return La liste des entreprises présentes dans la BD
     */
    @Override
    public List<Company> fetchList() {
        try {
            return this.companySearcher.fetchList();
        } catch (PersistanceException e) {
            LOG.error("fetchList: " + e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Renvoie la liste des instances présentes dans la BD qui sont contenues dans
     * la page en paramètre.
     *
     * @param page la page à afficher.
     * @return La liste des entreprises présentes dans la page.
     */
    @Override
    public List<Company> fetchList(@NonNull final Page page) {
        try {
            return this.companySearcher.fetchList(page);
        } catch (PersistanceException e) {
            LOG.error("Recherche de la liste : Exception reçue. Renvoi d'une liste vide");
            return new ArrayList<>();
        }
    }

    /**
     * Recherche d'une entreprise à partir de son identifiant.
     *
     * @param id l'identifiant recherché
     * @return un Optional contenant une instance de Company si une ligne
     *         correspondante a été trouvée dans la BD ou une instance de Optional
     *         vide si aucune entreprise n'a été trouvée
     */
    @Override
    public Optional<Company> findById(final long id) {
        try {
            return this.companySearcher.fetchById(id);
        } catch (PersistanceException e) {
            return Optional.empty();
        }
    }

    /**
     * @return le nombre d'entreprises dans la BD, ou -1 s'il y a eu un problème
     *         dans la base
     */
    @Override
    public int getNumberOfElements() {
        try {
            return this.companySearcher.getNumberOfElements();
        } catch (PersistanceException e) {
            LOG.error("getNbOfElements : " + e.getMessage(), e);
            return -1;
        }
    }

    /**
     * change l'instance de la couche persistance Uniquement utilisé pour les tests.
     *
     * @param newCompanySearcher la nouvelle
     */
    public void setCompanySearcher(final CompanySearcher newCompanySearcher) {
        this.companySearcher = newCompanySearcher;
    }

}