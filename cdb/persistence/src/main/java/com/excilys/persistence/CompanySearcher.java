package com.excilys.persistence;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.excilys.model.Company;
import com.excilys.model.Page;


/**
 * Classe utilisée pour les requêtes associées à la table company.
 *
 * @author jguyot2
 */
@Repository
public class CompanySearcher implements Searcher<Company> {

    private static final Logger LOG = LoggerFactory.getLogger(CompanySearcher.class);

    @PersistenceContext
    private EntityManager em;


    /**
     * Recherche d'un fabricant à partir de son identifiant dans la base de données.
     *
     * @param searchedId l'identifiant de l'entreprise identifiée
     *
     * @return Optional.empty() si aucune entreprise n'a été trouvée, ou une instance de Optional contenant
     *         l'entreprise trouvée
     *
     * @throws DaoException
     */
    @Override
    public Optional<Company> fetchById(final long searchedId) throws DaoException {
        try {
            CriteriaBuilder cb = this.em.getCriteriaBuilder();
            CriteriaQuery<Company> ct = cb.createQuery(Company.class);
            Root<Company> r = ct.from(Company.class);
            ct.select(r);
            ct.where(cb.equal(r.get("id"), searchedId));
            TypedQuery<Company> q = this.em.createQuery(ct);
            return Optional.of(q.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (PersistenceException e) {
            throw new DaoException();
        }
    }

    /**
     * Rend la liste des entreprises présentes dans la base de données, sous la forme d'instances de Company.
     *
     * @return La liste des entreprises présentes dans la base de données
     */
    @Override
    public List<Company> fetchList() throws DaoException {
        try {
            CriteriaBuilder cb = this.em.getCriteriaBuilder();
            CriteriaQuery<Company> ct = cb.createQuery(Company.class);
            Root<Company> r = ct.from(Company.class);
            ct.select(r);
            TypedQuery<Company> q = this.em.createQuery(ct);
            return q.getResultList();
        } catch (PersistenceException e) {
            throw new DaoException(e);
        }
    }

    /**
     * Recherche une "page" de la liste des entreprises, en fonction du paramètre.
     *
     * @param page la page à afficher
     *
     * @return la liste des Company de la BD comprises dans la page en paramètre
     *
     * @throws DaoException
     */
    @Override
    public List<Company> fetchList(@NonNull final Page page) throws DaoException {
        try {
            CriteriaBuilder cb = this.em.getCriteriaBuilder();
            CriteriaQuery<Company> ct = cb.createQuery(Company.class);
            Root<Company> r = ct.from(Company.class);
            ct.select(r);
            TypedQuery<Company> q = this.em.createQuery(ct)
                    .setFirstResult(page.getOffset())
                    .setMaxResults(page.getPageLength());
            return q.getResultList();
        } catch (PersistenceException e) {
            throw new DaoException(e);
        }
    }

    /**
     * renvoie la taille de la table company.
     *
     * @return le nombre d'entreprises enregistrées dans la base
     *
     * @throws DaoException
     */
    @Override
    public int getNumberOfElements() throws DaoException {
        try {
            CriteriaBuilder cb = this.em.getCriteriaBuilder();
            CriteriaQuery<Long> ct = cb.createQuery(Long.class);
            Root<Company> r = ct.from(Company.class);
            ct.select(cb.count(r));
            TypedQuery<Long> q = this.em.createQuery(ct);
            return q.getSingleResult().intValue();
        } catch (PersistenceException e) {
            throw new DaoException(e);
        }
    }
}
