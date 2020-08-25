





package com.excilys.persistence;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.model.Company;
import com.excilys.model.Computer;
import com.excilys.model.NotImplementedException;
import com.excilys.model.Page;
import com.excilys.model.sort.SortCriterion;
import com.excilys.model.sort.SortEntry;


/**
 * Classe permettant d'effectuer des requêtes sur des Computers.
 *
 * TODO refacto pour gérer les exceptions
 *
 * @author jguyot2
 */
@Repository
public class ComputerSearcher implements Searcher<Computer> {

     @PersistenceContext
     private EntityManager em;

     private static final Logger LOG = LoggerFactory.getLogger(ComputerSearcher.class);


     /**
      * Recherche un ordinateur dans la base de donnée, à partir d'un identifiant.
      *
      * @param searchedId L'id de l'ordinateur recherché
      *
      * @return une instance de Optional vide si aucun ordinateur de l'id donné n'est présent dans la BD ou
      *         qu'une exception SQLException s'est produite/ Une instance de Optional contenant le Computer
      *         trouvé sinon
      *
      * @throws PersistanceException
      */
     @Override
     @Transactional
     public Optional<Computer> fetchById(final long searchedId) throws PersistanceException {
          CriteriaBuilder cb = this.em.getCriteriaBuilder();
          CriteriaQuery<Computer> ct = cb.createQuery(Computer.class);
          Root<Computer> r = ct.from(Computer.class);
          ct.select(r);
          ct.where(cb.equal(r.get("id"), searchedId));
          TypedQuery<Computer> q = this.em.createQuery(ct);
          // Sale
          List<Computer> res = q.getResultList();
          return res.isEmpty() ? Optional.empty() : Optional.of(res.get(0));
     }

     /**
      * ct.orderBy(getCriteriaList(cb, r, entries)); Cherche tous les ordinateurs dans la base, et retourne la
      * liste correspondant à ces derniers.
      *
      * @return La liste des ordinateurs présents dans la base de données
      *
      * @author jguyot2
      *
      * @throws PersistanceException
      */
     @Transactional
     @Override
     public List<Computer> fetchList() throws PersistanceException {
          CriteriaBuilder cb = this.em.getCriteriaBuilder();
          CriteriaQuery<Computer> ct = cb.createQuery(Computer.class);
          Root<Computer> r = ct.from(Computer.class);
          ct.select(r);
          TypedQuery<Computer> q = this.em.createQuery(ct);
          return q.getResultList();
     }

     /**
      * Renvoie la liste des Computer "compris" dans la page en paramètre.
      *
      * @param page
      *
      * @return La liste correspondant aux ordinateurs compris dans la page.
      *
      * @throws PersistanceException
      */
     @Override
     @Transactional
     public List<Computer> fetchList(@NonNull final Page page) throws PersistanceException {
          return fetchList(page, Arrays.asList());
     }

     private <T, U> Order getCriteriaBuilderOrderByFromSortEntry(final CriteriaBuilder cb,
               final Root<Computer> r,
               final Join<Computer, Company> j, final SortEntry se) {
          SortCriterion criterion = se.getCriteria();
          switch (criterion) {
               case COMPUTER_NAME:
                    return se.isAscending() ? cb.asc(r.get("name")) : cb.desc(r.get("name"));
               case DISCONTINUED:
                    return se.isAscending() ? cb.asc(r.get("discontinued")) : cb.desc(r.get("discontinued"));
               case INTRODUCED:
                    return se.isAscending() ? cb.asc(r.get("introduced")) : cb.desc(r.get("introduced"));
               case COMPANY_ID:
                    return se.isAscending() ? cb.asc(j.get("id")) : cb.desc(r.get("id"));
               case COMPANY_NAME:
                    return se.isAscending() ? cb.asc(j.get("name")) : cb.desc(j.get("name"));
               default:
                    throw new NotImplementedException();

          }
     }

     @Transactional
     public List<Computer> fetchList(@NonNull final Page page, @NonNull final List<SortEntry> entries)
               throws PersistanceException {
          CriteriaBuilder cb = this.em.getCriteriaBuilder();
          CriteriaQuery<Computer> ct = cb.createQuery(Computer.class);
          Root<Computer> r = ct.from(Computer.class);
          Join<Computer, Company> j = r.join("manufacturer", JoinType.LEFT);
          ct.select(r);
          ct.orderBy(getCriteriaList(cb, r, j, entries));
          TypedQuery<Computer> q = this.em.createQuery(ct).setFirstResult(page.getOffset())
                    .setMaxResults(page.getPageLength());

          return q.getResultList();
     }

     @Transactional
     public List<Computer> fetchList(@NonNull final Page p, @NonNull final String search)
               throws PersistanceException {
          return fetchList(p, search, Arrays.asList());
     }

     private Order[] getCriteriaList(final CriteriaBuilder cb, final Root<Computer> r,
               final Join<Computer, Company> j,
               final List<SortEntry> se) {
          Order[] order = new Order[se.size() + 1];
          for (int i = 0; i < se.size(); ++i) {
               order[i] = getCriteriaBuilderOrderByFromSortEntry(cb, r, j, se.get(i));
          }
          order[order.length - 1] = cb.asc(r.get("id"));
          return order;
     }

     @Transactional
     public List<Computer> fetchList(@NonNull final Page page, @NonNull final String search,
               @NonNull final List<SortEntry> entries) throws PersistanceException {
          CriteriaBuilder cb = this.em.getCriteriaBuilder();
          CriteriaQuery<Computer> ct = cb.createQuery(Computer.class);
          Root<Computer> r = ct.from(Computer.class);
          Join<Computer, Company> join = r.join("manufacturer", JoinType.LEFT);

          ct.multiselect(r, join)
                    .where(cb.like(r.get("name"), "%" + search.replace("%", "\\%") + "%"))
                    .orderBy(getCriteriaList(cb, r, join, entries));
          TypedQuery<Computer> q = this.em.createQuery(ct)
                    .setFirstResult(page.getOffset())
                    .setMaxResults(page.getPageLength());
          List<Computer> cList = q.getResultList();
          System.out.println(cList);

          return cList;
     }

     /**
      * @return Le nombre d'éléments computer enregistrés dans la base.
      *
      * @throws PersistanceException
      */
     @Transactional
     @Override
     public int getNumberOfElements() throws PersistanceException {
          CriteriaBuilder cb = this.em.getCriteriaBuilder();
          CriteriaQuery<Long> ct = cb.createQuery(Long.class);
          Root<Computer> r = ct.from(Computer.class);
          ct.select(cb.count(r));

          TypedQuery<Long> q = this.em.createQuery(ct);
          return q.getSingleResult().intValue();
     }

     @Transactional
     public int getNumberOfFoundElements(@NonNull final String search) throws PersistanceException {
          CriteriaBuilder cb = this.em.getCriteriaBuilder();
          CriteriaQuery<Long> ct = cb.createQuery(Long.class);
          Root<Computer> r = ct.from(Computer.class);
          ct.select(cb.count(r));
          ct.where(cb.like(r.get("name"), "%" + search.replace("%", "\\%") + "%"));
          TypedQuery<Long> q = this.em.createQuery(ct);
          return q.getSingleResult().intValue();
     }

     @Transactional
     public List<Computer> searchByName(@NonNull final String search) throws PersistanceException {
          CriteriaBuilder cb = this.em.getCriteriaBuilder();
          CriteriaQuery<Computer> ct = cb.createQuery(Computer.class);
          Root<Computer> r = ct.from(Computer.class);
          Join<Computer, Company> j = r.join("manufacturer", JoinType.LEFT);
          ct.select(r);
          ct.where(cb.like(r.get("name"), "%" + search.replace("%", "\\%") + "%"));
          TypedQuery<Computer> q = this.em.createQuery(ct);
          return q.getResultList();
     }

     @Transactional
     public List<Computer> fetchList(@NonNull final List<SortEntry> entries) throws PersistanceException {
          CriteriaBuilder cb = this.em.getCriteriaBuilder();
          CriteriaQuery<Computer> ct = cb.createQuery(Computer.class);
          Root<Computer> r = ct.from(Computer.class);
          Join<Computer, Company> j = r.join("manufacturer", JoinType.LEFT);
          ct.select(r);
          ct.orderBy(getCriteriaList(cb, r, j, entries));
          TypedQuery<Computer> q = this.em.createQuery(ct);
          return q.getResultList();
     }
}
