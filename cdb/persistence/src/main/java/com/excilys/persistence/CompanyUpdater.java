package com.excilys.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.model.Company;


/**
 * Gestion de la mise à jour des entreprises dans la base
 *
 * @author jguyot2
 */
@Repository
public class CompanyUpdater {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyUpdater.class);

    @Autowired
    private ComputerUpdater computerUpdater;

    @PersistenceContext
    private EntityManager em;


    /**
     * Suppression d'une entreprise à partir de son identifiant, ainsi que tous les ordinateurs liés à cette
     * entreprise
     *
     * @param companyId l'identifiant de l'entreprise à supprimer.
     *
     * @throws DaoException en cas d'erreur dans la base.
     */
    @Transactional(rollbackFor = DaoException.class)
    public void deleteCompany(final long companyId) throws DaoException {
        CompanyUpdater.LOG.trace("Deleting computers of company " + companyId);
        this.computerUpdater.deleteComputersOfCompany(companyId);
        CompanyUpdater.LOG.trace("Deleting company " + companyId);
        int numberOfDeletedCompanies = deleteCompanyById(companyId);
        if (numberOfDeletedCompanies == 0) {
            throw new DaoException();
        }
    }

    private int deleteCompanyById(final long companyId) throws DaoException {
        try {
            CriteriaBuilder cb = this.em.getCriteriaBuilder();
            CriteriaDelete<Company> companyDeleteCriteria = cb.createCriteriaDelete(Company.class);
            Root<Company> r = companyDeleteCriteria.from(Company.class);
            companyDeleteCriteria.where(cb.equal(r.get("id"), companyId));
            return this.em.createQuery(companyDeleteCriteria).executeUpdate();
        } catch (PersistenceException e) {
            throw new DaoException(e);
        }
    }
}
