package com.excilys.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.model.Company;
import com.excilys.model.Page;
import com.excilys.persistence.CompanySearcher;

public class CompanyValidator implements SearchValidator<Company> {
    /** */
    private static final Logger LOG = LoggerFactory.getLogger(CompanyValidator.class);

    /** */
    private CompanySearcher companySearcher;

    /** */
    public CompanyValidator() {
        this.companySearcher = new CompanySearcher();
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
        } catch (SQLException e) {
            LOG.error("fetchList: " + e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Renvoie la liste des instances présentes dans la BD qui sont contenues dans la
     * page en paramètre.
     *
     * @param page la page à afficher.
     * @return La liste des entreprises présentes dans la page.
     */
    @Override
    public List<Company> fetchWithOffset(final Page page) {
        try {
            return this.companySearcher.fetchWithOffset(page);
        } catch (SQLException e) {
            LOG.error("Recherche de la liste : Exception reçue. Renvoi d'une liste vide");
            return new ArrayList<>();
        }
    }

    /**
     * Recherche d'une entreprise à partir de son identifiant.
     *
     * @param id l'identifiant recherché
     * @return une instance de Optional contenant une instance de Company si une
     *         ligne correspondante a été trouvée dans la BD ou une instance de
     *         Optional vide si aucune entreprise n'a été trouvée
     */
    @Override
    public Optional<Company> findById(final long id) {
        try {
            return this.companySearcher.fetchById(id);
        } catch (SQLException e) {
            LOG.debug("findById" + e.getMessage(), e);
            return Optional.empty();
        }
    }

    /**
     * @return le nombre d'entreprises dans la BD, ou -1 s'il y a eu un problème dans
     *         la base
     */
    @Override
    public int getNumberOfElements() {
        try {
            return this.companySearcher.getNumberOfElements();
        } catch (SQLException e) {
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
